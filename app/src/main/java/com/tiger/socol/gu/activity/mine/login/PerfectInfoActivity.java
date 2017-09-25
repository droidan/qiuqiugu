package com.tiger.socol.gu.activity.mine.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.utils.StringUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.bind.PlatInfo;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.utils.FileUtlis;
import com.tiger.socol.gu.utils.MediaUtils;
import com.tiger.socol.gu.utils.PermissionUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


public class PerfectInfoActivity extends BaseViewStateActivity<PerfectInfoView, PerfectInfoPresenter>
        implements PerfectInfoView, PermissionUtils.onPermissionResultListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.im_avatar)
    RoundedImageView imAvatar;
    @BindView(R.id.ed_name)
    EditText edName;

    private PlatInfo platInfo;

    private String avatarPath;
    private String phone;
    private String password;
    private PermissionUtils permissionUtils;

    public static final String PASSWORD_KEY = "";

    private final static int REQUEST_CAMERA = 600;
    private final static int REQUEST_GALLERY = 601;
    private final static int REQUEST_CUTPHOTO = 602;

    @NonNull
    @Override
    public PerfectInfoPresenter createPresenter() {
        return new PerfectInfoPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_perfect_info;
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra(IntentConstant.PHONE);
        password = getIntent().getStringExtra(PASSWORD_KEY);
        platInfo = getIntent().getParcelableExtra(IntentConstant.PLAT_INFO);
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (platInfo != null) {
            edName.setText(platInfo.getNikeName());
            Picasso.with(this).load(platInfo.getUserAvatar()).into(imAvatar);
        }
    }

    @OnClick({R.id.im_avatar, R.id.bt_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_avatar:
                showPhotoDialog();
                break;

            case R.id.bt_submit:
                String name = edName.getText().toString();
                if (StringUtils.isEmpty(name)) {
                    showToask("请输入昵称");
                    return;
                }

//                if (StringUtils.isEmpty(avatarPath)) {
//                    showToask("请选择头像");
//                    return;
//                }
                showProgress();

                ArrayList<String> file = new ArrayList<>();
                file.add(avatarPath);
                showProgress();
                presenter.upload(file);
                break;
        }
    }

    @Override
    public void onRegistSuccess(Member member) {
        dismissProgress();
        showToask("注册成功");
        EventBus.getDefault().post(new LoginEvent());
        finish();
    }

    @Override
    public void onRegistFailure(String message) {
        dismissProgress();
        showToask("注册失败，请重试");
    }

    @Override
    public void onLoadImageSuccess(String avatar) {
        presenter.regist(phone, password, avatar, edName.getText().toString());
    }

    @Override
    public void onLoadImageFailure(String message) {
        dismissProgress();
        showToask("注册失败，请重试");
    }

    private void showPhotoDialog() {
        permissionUtils = PermissionUtils.getIntence(this)
                .setPermissionResultListener(this)
                .requestPerssion(PermissionUtils.Permission_STORAGE);
    }

    @Override
    public void onRefuse() {
        showToask("没有权限");
    }

    @Override
    public void onPermit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("请选择照片"); //设置内容
        builder.setPositiveButton("相机拍摄", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MediaUtils.startCamera(PerfectInfoActivity.this, REQUEST_CAMERA, "user.jpg");
            }
        });
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MediaUtils.selectImageFromAlbum(PerfectInfoActivity.this, REQUEST_GALLERY);
            }
        });
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CAMERA:
                MediaUtils.startCameraToCutPicture(PerfectInfoActivity.this, REQUEST_CUTPHOTO, "user.jpg");
                break;

            case REQUEST_GALLERY:
                MediaUtils.startCameraToCutPicture(PerfectInfoActivity.this, REQUEST_CUTPHOTO, data.getData());
                break;

            case REQUEST_CUTPHOTO:
                afterCutPhoto(data);
                break;
        }
    }

    /**
     * 从拍 截图结束后续操作
     */
    private void afterCutPhoto(Intent data) {
        Bitmap bitmap = null;
        if (data == null) {
            showToask("获取照片失败");
            return;
        }

        Uri uri = data.getData();
        if (uri != null) {
            // 从文件路径中读取图片文件
            bitmap = BitmapFactory.decodeFile(uri.getPath());
        }

        if (bitmap == null) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                showToask("获取照片失败");
                return;
            } else {
                bitmap = (Bitmap) bundle.get("data");
            }
        }

        if (bitmap == null) {
            showToask("获取照片失败");
            return;
        }

        final Bitmap fb = bitmap;
        imAvatar.setImageBitmap(fb);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 保存图片到SD卡
                String bitmapStr = FileUtlis.getIntence().saveBitmap(PerfectInfoActivity.this, fb);
                // 上传图片
                avatarPath = bitmapStr;
            }
        }).start();

        // 释放图片内存
        if (bitmap.isRecycled()) {
            fb.recycle();
            bitmap.recycle();
        }
    }

}

