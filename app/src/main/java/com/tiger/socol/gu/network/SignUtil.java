package com.tiger.socol.gu.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 签名工具
 * Created by jian_zhou on 2016/9/8.
 */
public class SignUtil {

    /**
     * 数据签名规则 1. 获得GET参数，形成数组； 2. 获得POST参数，加入数组，如果已经存在相同键名的值，覆盖； 3. 对参数数组按键名排序；
     * 4. 使用http_build_query对数组处理形成字符串，既使数组为空，也要进行此步骤； 5.
     * 对字符串进行md5计算，得到的32位值即为第一次sign签名的值； 6. 将sign附加私钥(secret)，和当前时间戳，形成新的字符串； 7.
     * 对新字符串进行md5计算，得到的32位值即为最终的sign签名的值
     */
    public static String getSignStr(Map<String, String> paramsMap, String time) {
        // 1排序
        LinkedHashMap<String, String> sortParams = new LinkedHashMap<>();
        Object[] key_arr = paramsMap.keySet().toArray();
        Arrays.sort(key_arr);
        for (Object key : key_arr) {
            try {
                sortParams.put(key.toString(), URLEncoder.encode(paramsMap.get(key).toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // 2 hashmap.toString()
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : sortParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        // 3 md5
        String resultMD5 = MD5.md5(result.toString().replace("*", "%2A").replace("%7E", "~").replace("+", "%20"));

        // 4 MD5+secret+time
        StringBuilder sbAppend = new StringBuilder();
        sbAppend.append(resultMD5);
        sbAppend.append(Config.Secret);
        sbAppend.append(time);

        String s = MD5.md5(sbAppend.toString());
        // 5 MD5
        return s;
    }

}