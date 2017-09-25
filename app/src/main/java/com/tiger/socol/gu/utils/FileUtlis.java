package com.tiger.socol.gu.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jian_zhou on 2016/5/11.
 */
public class FileUtlis {

    public String sd_card = Environment.getExternalStorageDirectory().getPath() + "/Yst";
    /**
     * 图片的文件夹
     */
    public String localFildPathImage = "/";
    /**
     * crash
     */
    public String crash = "crash/";

    public static FileUtlis fileUtils;

    public static FileUtlis getIntence() {
        if (fileUtils == null)
            fileUtils = new FileUtlis();
        return fileUtils;
    }

    public void initFile(Activity aty) {
        if (!isExistSDCard())
            sd_card = aty.getFilesDir().getPath().toString();
        File imageFile = new File(sd_card + localFildPathImage);
        if (!imageFile.exists())
            imageFile.mkdirs();
    }

    public String getImageLocalPath() {
        return sd_card + localFildPathImage;
    }

    /**
     * 判断sdk是否可用
     *
     * @return
     */
    private boolean isExistSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public String saveBitmap(final Context context, final Bitmap bitmap) {
        BufferedOutputStream os = null;
        try {
            File filePath = new File(sd_card + localFildPathImage + "header"
                    + ".png");
            if (!filePath.exists()) {
                filePath.createNewFile();
            }

            os = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            MediaUtils.afterSaveImage(context, filePath);

            return filePath.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
