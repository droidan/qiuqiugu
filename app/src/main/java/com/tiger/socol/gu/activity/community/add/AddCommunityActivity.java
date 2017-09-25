package com.tiger.socol.gu.activity.community.add;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.utils.StringUtils;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.community.PhotoInfo;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.views.widgets.MultiImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class AddCommunityActivity extends BaseViewStateActivity<AddCommunityView, AddCommunityPresenter>
        implements AddCommunityView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.miv_select_images)
    MultiImageView mivSelectImages;

    ArrayList<String> selectedPhotos = new ArrayList<>();

    @NonNull
    @Override
    public AddCommunityPresenter createPresenter() {
        return new AddCommunityPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_community;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("添加");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String content = edContent.getText().toString();
                if (StringUtils.isEmpty(content)) {
                    showToask("请输入内容");
                    return true;
                }

                showProgress();

                if (selectedPhotos.size() == 0) {
                    presenter.add(content, null);
                } else {
                    presenter.upload(selectedPhotos);
                }
                return true;
            }
        });

        mivSelectImages.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (selectedPhotos.size() == 9) {
                    previewPhoto(position);
                } else {
                    if (position == selectedPhotos.size() || selectedPhotos.size() == 0) {
                        selectPhotos();
                    } else {
                        previewPhoto(position);
                    }
                }
            }
        });

        showPhoto();
    }

    public void selectPhotos() {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setShowGif(false)
                .setSelected(selectedPhotos)
                .setPreviewEnabled(true)
                .start(AddCommunityActivity.this, PhotoPicker.REQUEST_CODE);
    }

    public void previewPhoto(int position) {
        PhotoPreview.builder()
                .setPhotos(selectedPhotos)
                .setCurrentItem(position)
                .start(AddCommunityActivity.this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            selectedPhotos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            showPhoto();
        }
    }

    private void showPhoto() {
        ArrayList<PhotoInfo> sp = new ArrayList<>();
        for (int a = 0; a < selectedPhotos.size(); a++) {
            String p = selectedPhotos.get(a);
            StringBuilder sb = new StringBuilder();
            sb.append("file://");
            sb.append(p);

            PhotoInfo info = new PhotoInfo();
            info.url = sb.toString();
            sp.add(info);
        }

        if (selectedPhotos.size() != 9) {
            PhotoInfo info = new PhotoInfo();
            info.resid = R.drawable.bt_add_photos;
            info.url = "";
            sp.add(info);
        }

        mivSelectImages.setList(sp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public void onLoadImageSuccess(ArrayList<String> images) {
        String content = edContent.getText().toString();
        presenter.add(content, images);
    }

    @Override
    public void onLoadImageFailure(String message) {

    }

    @Override
    public void onAddSuccess(CommentEntity value) {
        dismissProgress();
        finish();
        showToask("发布成功");
    }

    @Override
    public void onAddFailure(String message) {
        dismissProgress();
        showToask("发布失败");
    }

}

