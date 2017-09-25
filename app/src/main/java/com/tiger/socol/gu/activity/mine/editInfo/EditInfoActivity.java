package com.tiger.socol.gu.activity.mine.editInfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.address.edit.EditAddressActivity;
import com.tiger.socol.gu.activity.mine.address.list.AddressListActivity;
import com.tiger.socol.gu.activity.mine.editInfo.name.EditNameActivity;
import com.tiger.socol.gu.activity.mine.login.PerfectInfoActivity;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.utils.FileUtlis;
import com.tiger.socol.gu.utils.MediaUtils;
import com.tiger.socol.gu.utils.PermissionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class EditInfoActivity extends BaseViewStateActivity<EditInfoView, EditInfoPresenter>
        implements EditInfoView, PermissionUtils.onPermissionResultListener {

    @BindView(R.id.im_avatar)
    RoundedImageView imAvatar;
    @BindView(R.id.tx_ed_name)
    TextView txEdName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private PermissionUtils permissionUtils;
    private String avatarPath;

    private final static int REQUEST_CAMERA = 600;
    private final static int REQUEST_GALLERY = 601;
    private final static int REQUEST_CUTPHOTO = 602;

    @NonNull
    @Override
    public EditInfoPresenter createPresenter() {
        return new EditInfoPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_info;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("修改资料");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Member member = MemberMannager.getInstance().getLoginUser();
        txEdName.setText(member.getNickName());
        if (!StringUtils.isEmpty(member.getAvatar())) {
            Picasso.with(this).load(member.getAvatar()).into(imAvatar);
        }
    }

    @OnClick({R.id.rl_ei_avatar, R.id.rl_ei_name, R.id.rl_ei_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_ei_avatar:
                // 修改头像
                showPhotoDialog();
                break;

            case R.id.rl_ei_name:
                // 修改名称
                startActi(EditNameActivity.class);
                break;

            case R.id.rl_ei_address:
                // 修改收货地址
                startActi(AddressListActivity.class);
                break;
        }
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
                MediaUtils.startCamera(EditInfoActivity.this, REQUEST_CAMERA, "user.jpg");
            }
        });
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MediaUtils.selectImageFromAlbum(EditInfoActivity.this, REQUEST_GALLERY);
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
                MediaUtils.startCameraToCutPicture(EditInfoActivity.this, REQUEST_CUTPHOTO, "user.jpg");
                break;

            case REQUEST_GALLERY:
                MediaUtils.startCameraToCutPicture(EditInfoActivity.this, REQUEST_CUTPHOTO, data.getData());
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 保存图片到SD卡
                String bitmapStr = FileUtlis.getIntence().saveBitmap(EditInfoActivity.this, fb);
                // 上传图片
                avatarPath = bitmapStr;

                ArrayList<String> file = new ArrayList<>();
                file.add(avatarPath);
                presenter.upload(file);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress();
                    }
                });
            }
        }).start();

        // 释放图片内存
        if (bitmap.isRecycled()) {
            fb.recycle();
            bitmap.recycle();
        }
    }

    @Override
    public void onLoadImageSuccess(String avatar) {
        presenter.changeAvatar(avatar);
    }

    @Override
    public void onLoadImageFailure(String message) {
        dismissProgress();
        showToask("上传失败");
    }

    @Override
    public void onEditAvatarSuccess() {
        dismissProgress();
        onResume();
    }

    @Override
    public void onEditAvatarFailure(String message) {
        dismissProgress();
        showToask("上传失败");
    }

}

