package com.tiger.socol.gu.network;

import android.net.Uri;
import android.util.Log;

import com.blankj.utilcode.utils.StringUtils;
import com.tiger.socol.gu.managers.MemberMannager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Agent {

    private Agent() {
    }

    private static Agent single = null;

    //静态工厂方法
    public static Agent getInstance() {
        if (single == null) {
            single = new Agent();
        }
        return single;
    }

    public String buildRequestURL(Method method, String URL, String api, Map<String, String> ps, List<Module> ms) {
        String requestURL = URL + api;
        // 添加模块参数
        addModulesParament(ps, ms);
        // 添加通用参数
        addCommonPamameters(ps);

        if (method == Method.POST) {
            return requestURL;
        }

        // 拼接URL
        Uri.Builder builder = Uri.parse(requestURL).buildUpon();
        Set<String> keys = ps.keySet();
        for (String key : keys) {
            builder.appendQueryParameter(key, ps.get(key));
        }
        return builder.build().toString();
    }

    public void addModulesParament(Map<String, String> ps, List<Module> ms) {
        StringBuffer sb = new StringBuffer();
        for (Module m : ms) {
            sb.append(m.name);
//            sb.append(":");
//            sb.append(m.version + "");
            sb.append(",");
        }

        ps.put("module", sb.substring(0, sb.length() - 1));
    }

    public void addCommonPamameters(Map<String, String> ps) {
        String time = System.currentTimeMillis() + "";
        ps.put("time", time);
        ps.put("ip", AppUtil.getLocalIP());
        ps.put("type", "android");
        ps.put("version", "1.0.0");
        ps.put("deviceId", "112");
        if (MemberMannager.getInstance().isLogin()) {
            ps.put("userId", MemberMannager.getInstance().getUserid());
        }
        ps.put("token", SignUtil.getSignStr(ps, time));
    }

    private RequestBody buildFormBody(Map<String, String> ps) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : ps.keySet()) {
            builder.add(key, ps.get(key));
        }
        return builder.build();
    }

    private RequestBody buildFileBody(Map<String, String> ps, Map<String, File> fs) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        // 添加文件参数
        for (String key : fs.keySet()) {
            File file = fs.get(key);
            builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
        }

        // 添加表单参数
        for (String key : ps.keySet()) {
            builder.addFormDataPart(key, ps.get(key));
        }
        return builder.build();
    }

    public Call bulidPostCall(Request objectRequest) {
        List<Module> ms = new ArrayList<>();
        Map<String, String> ps = new HashMap<>();
        Map<String, File> fs = new HashMap<>();
        // 获取参数
        objectRequest.parament(ps);
        // 获取模块
        objectRequest.modules(ms);
        // 获取文件
        objectRequest.files(fs);
        // 请求地址
        final String requestURL = buildRequestURL(objectRequest.method(), objectRequest.URL(), objectRequest.api(), ps, ms);
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        builder.url(requestURL);
        // 如果没有文件使用buildFormBody
        if (fs.size() == 0) {
            builder.post(buildFormBody(ps));
        } else {
            builder.post(buildFileBody(ps, fs));
        }
        okhttp3.Request request = builder.build();
        OkHttpClient httpClient = new OkHttpClient();
        // 设置请求超时
        httpClient.newBuilder().connectTimeout(60, TimeUnit.SECONDS);
        Call call = httpClient.newCall(request);
        return call;
    }

    public <T> void requestArray(ArrayRequest request, final ArrayRequest.OnRequestListeren<T> listener) {
        final Platform platform = Platform.get();
        Call call = bulidPostCall(request);
        final Class<T> clazz = (Class<T>) getInterfaceType(listener.getClass());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendError("请求失败", listener);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = responseJson(response, listener);
                if (data == null) {
                    return;
                }
                final List<T> t = GsonUtil.jsonToList(data, clazz);
                platform.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(t);
                    }
                });
            }
        });
    }

    public <T> void requestObject(ObjectRequest request, final ObjectRequest.OnRequestListeren<T> listener) {
        final Platform platform = Platform.get();
        Call call = bulidPostCall(request);
        final Class<T> clazz = (Class<T>) getInterfaceType(listener.getClass());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendError("请求失败", listener);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = responseJson(response, listener);
                if (StringUtils.isEmpty(data)) {
                    return;
                }

                final T t = GsonUtil.GsonToBean(data, clazz);
                platform.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(t);
                    }
                });
            }
        });
    }

    public String responseJson(Response response, final OnRequestErrorListeren listeren) {
        try {
            Log.e("URL", response.request().url().toString());
            String json = response.body().string();
            Log.e("json", json);
            JSONObject object = new JSONObject(json);
            boolean state = object.getBoolean("state");
            if (!state) {
                String error = object.getString("error");
                sendError(error, listeren);
                return null;
            }

            String data = object.getString("data");
            if (data == null) {
                sendError("请求失败", listeren);
                return null;
            }
            return data;
        } catch (Exception e) {
            sendError("请求失败", listeren);
            return null;
        }
    }

    public void sendError(final String error, final OnRequestErrorListeren listeren) {
        final Platform platform = Platform.get();
        platform.execute(new Runnable() {
            @Override
            public void run() {
                listeren.onFailure(error);
            }
        });
    }

    public void requestNull(NullRequest request, final NullRequest.OnRequestListeren listeren) {
        final Platform platform = Platform.get();
        Call call = bulidPostCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendError("请求失败", listeren);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = responseJson(response, listeren);
                if (data == null) {
                    return;
                }
                platform.execute(new Runnable() {
                    @Override
                    public void run() {
                        listeren.onSuccess();
                    }
                });
            }
        });
    }

    public <T> void requestFlow(FlowRequest request, final FlowRequest.OnRequestListeren<T> listener) {
        final Platform platform = Platform.get();
        Call call = bulidPostCall(request);
        final Class<T> clazz = (Class<T>) getInterfaceType(listener.getClass());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                platform.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure("请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = responseJson(response, listener);
                if (data == null) {
                    return;
                }

                try {
                    JSONObject json = new JSONObject(data);
                    int total = json.getInt("total");
                    boolean nextPage = json.getBoolean("nextPage");
                    String i = json.getString("data");

                    final List<T> items = GsonUtil.jsonToList(i, clazz);
                    final FlowEntity<T> flow = new FlowEntity<>();
                    flow.setTotal(total);
                    flow.setData(items);
                    flow.setNextPage(nextPage);

                    platform.execute(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(flow);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError("请求失败", listener);
                }

            }
        });
    }

    /**
     * 获取接口泛型
     *
     * @param c class
     * @return Type
     */
    private Type getInterfaceType(Class<?> c) {
        Type genericType = null;
        Type[] genericInterfaces = c.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                genericType = genericTypes[0];
            }
        }
        return genericType;
    }

}
