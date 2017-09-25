package com.tiger.socol.gu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by jian_zhou on 2016/5/11.
 */
public class MediaUtils {

    /**
     * 调用系统照相机
     *
     * @param activity
     * @param requestCode 请求码
     * @Title: startCamera
     * @Description: TODO
     * @return: void
     */
    public static void startCamera(Activity activity, int requestCode, String photoName) {
        if (checkCameraHardware(activity)) {
            String sd_card = FileUtlis.getIntence().getImageLocalPath();
            File floder = new File(sd_card);
            if (!floder.getParentFile().exists() || !floder.getParentFile().isDirectory()) {
                floder.getParentFile().mkdirs();
            }

            if (!floder.exists()) {
                floder.mkdirs();
            }

            File filePath = new File(sd_card + photoName);
            if (!filePath.exists()) {
                try {
                    filePath.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
                    Toast.makeText(activity, "请检查文件读写权限是否开启", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = Uri.fromFile(filePath);
                if (checkCameraAutofocus(activity)) {
                    intent.putExtra("autofocus", true); // 自动对焦
                }

                if (photoUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    activity.startActivityForResult(intent, requestCode);
                }
            } catch (Exception e) {
                Toast.makeText(activity, "请查看摄像头权限", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(activity, "设备没有摄像头", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 拍照结束后后通知系统相册
     *
     * @param context
     * @param imagePath
     */
    public static void afterCamera(Context context, String imagePath) {
        afterSaveImage(context, new File(imagePath));
    }

    /**
     * 保存图片后通知系统相册
     *
     * @param context
     * @param imagePath
     */
    public static void afterSaveImage(Context context, String imagePath) {
        afterSaveImage(context, new File(imagePath));
    }

    /**
     * 保存图片后通知系统相册
     *
     * @param context
     * @param file
     */
    public static void afterSaveImage(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 设备支持的摄像头是否支持自动对焦
     *
     * @param activity
     * @return
     * @Title: checkCameraAutofocus
     * @Description: TODO
     * @return: boolean
     */
    private static boolean checkCameraAutofocus(Activity activity) {
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }

    /**
     * 设备是否有摄像头
     *
     * @param activity
     * @return
     * @Title: checkCameraHardware
     * @Description: TODO
     * @return: boolean
     */
    private static boolean checkCameraHardware(Activity activity) {
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 剪切图片
     *
     * @param activity
     * @param photoName   图片名称
     * @param requestCode 请求码
     * @return
     * @Title: startCameraToCutPicture
     * @Description: TODO
     */
    public static void startCameraToCutPicture(Activity activity, int requestCode, String photoName) {
        Uri uri = null;
        File f = new File(FileUtlis.getIntence().getImageLocalPath() + photoName);
        if (f != null) {
            uri = Uri.fromFile(f);
        }
        startCameraToCutPicture(activity, requestCode, uri);
    }

    /**
     * 剪切图片
     *
     * @param activity
     * @param requestCode 请求码
     * @param uri         图片的Uri
     */
    public static void startCameraToCutPicture(Activity activity, int requestCode, Uri uri) {
        if (uri != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            // 需要裁减的图片格式
            intent.setDataAndType(uri, "image/*");
            // 允许裁减
            intent.putExtra("crop", "true");
            // 剪裁后ImageView显时图片的宽
            intent.putExtra("outputX", 240);
            // 剪裁后ImageView显时图片的高
            intent.putExtra("outputY", 240);
            // 设置剪裁框的宽高比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);// 若为false则表示不返回数据
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 从图库中选择图片
     *
     * @param activity
     * @param requestCode
     */
    public static void selectImageFromAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 判断sdk是否可用
     *
     * @return
     */
    public static boolean isExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

}