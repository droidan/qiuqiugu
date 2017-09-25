package com.tiger.socol.gu.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppUtil {
    // / 没有连接
    public static final int NETWORN_NONE = 0;
    // / wifi连接
    public static final int NETWORN_WIFI = 1;
    // / 3G连接
    public static final int NETWORN_MOBILE = 2;

    /**
     * 设备的名称
     */
    public static final String FileName = "equidname";
    /**
     * 当前选中的屏体信息类
     */
    public static final String EquipEntity = "entity";

    /* byte[]转Int */
    public static int bytesToInt(byte[] bytes) {
        int addr = bytes[0] & 0xFF;
        addr |= ((bytes[1] << 8) & 0xFF00);
        addr |= ((bytes[2] << 16) & 0xFF0000);
        addr |= ((bytes[3] << 25) & 0xFF000000);
        return addr;

    }

    /* Int转byte[] */
    public static byte[] intToByte(int i) {
        byte[] abyte0 = new byte[4];
        abyte0[0] = (byte) (0xff & i);
        abyte0[1] = (byte) ((0xff00 & i) >> 8);
        abyte0[2] = (byte) ((0xff0000 & i) >> 16);
        abyte0[3] = (byte) ((0xff000000 & i) >> 24);
        return abyte0;
    }

    /**
     * 获取手机的SerialNumber
     *
     * @return result is same to getSerialNumber1()
     */
    public static String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     * 判断当前否联网
     *
     * @param context 上下文
     * @return true联网, 否则没联网
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        if (!wifi) {
        }
        return true;
    }

    /**
     * 是否是wifi情况下
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        if (!wifi) {
            return wifi;
        }
        return true;
    }

    /**
     * 返回当前网络连接类型
     *
     * @param context 上下文
     * @return
     */
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }
        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getSystomDate() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);

        return str;

    }


    /**
     * 获取app的版本
     *
     * @param context
     * @return
     */
    public static int getAppPackNum(Context context) {
        int packageNum;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            packageNum = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            packageNum = -1;
        }
        return packageNum;
    }

    public static String getAppCodeName(Context context) {
        String CodeName;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            CodeName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            CodeName = "";
        }
        return CodeName;
    }

    /**
     * 获取设备唯一编号
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        return DEVICE_ID;
    }

    /**
     * @param context
     * @return
     */
    public static String getAndrodID(Context context) {

        return android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

    }


    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }


    /**
     * @param checkData
     * @return
     * @author
     */
    public static boolean isObjectDataNull(Object checkData) {
        if ("".equals(checkData) || checkData == null) {
            return true;
        }
        return false;
    }

    public static boolean isTwoStringEqual(String aString, String bString) {
        if (aString.equals(bString) || bString == aString) {
            return true;
        }
        return false;
    }

    /**
     * 获取资源文件数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return
     */
    public static boolean isMobilePhone(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            String oneNum = str.substring(0, 1);
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (isNum.matches() && "1".equals(oneNum))
                return true;
            else
                return false;
        }
    }


    /**
     * 邮箱验证
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "没有发现版本号";
        }
    }

    /**
     * 获取屏幕宽
     *
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return displayMetrics.widthPixels;
    }


    public static int getSW(Activity context) {
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高
     *
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 获取IP
     *
     * @return
     */
    public static String getLocalIP() {
        String baseString = "100.100.100.100";
        String mLocalIp = getLocalIpAddress();
        if (isEmpty(mLocalIp)) {
            return baseString;
        }
        try {
            String localVersionloacl[] = mLocalIp.split("\\.");
            if (isObjectDataNull(localVersionloacl)) {
                return baseString;
            }
            if (localVersionloacl.length != 4) {
                return baseString;
            }
            if (isTwoStringEqual("0", localVersionloacl[0])
                    && isTwoStringEqual("0", localVersionloacl[1])
                    && isTwoStringEqual("0", localVersionloacl[2])
                    && isTwoStringEqual("0", localVersionloacl[3])) {
                return baseString;
            }
        } catch (Exception e) {
            return baseString;
        }

        return mLocalIp;
    }


    /**
     * 获取当前的ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            return "100.100.100.100";
        }
        return null;
    }


    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

}
