package com.tiger.socol.gu.activity.community.add;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.bbs.AddApi;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.api.upload.ImageInfoEntity;
import com.tiger.socol.gu.api.upload.UploadImageApi;
import com.tiger.socol.gu.api.upload.UploadImageEntity;
import com.tiger.socol.gu.network.ObjectRequest;

import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class AddCommunityPresenter extends MvpBasePresenter<AddCommunityView> {

    private Context context;

    public AddCommunityPresenter(Context context) {
        this.context = context;
    }

    public void upload(final ArrayList<String> files) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<File> fs = new ArrayList<>();
                for (String p : files) {
                    File file = new File(p);
                    file = Compressor.getDefault(context).compressToFile(file);
                    fs.add(file);
                }

                UploadImageApi api = new UploadImageApi();
                api.fields = fs;
                api.request(new ObjectRequest.OnRequestListeren<UploadImageEntity>() {
                    @Override
                    public void onSuccess(UploadImageEntity value) {
                        if (getView() == null) return;
                        ArrayList<String> images = new ArrayList<String>();
                        for (ImageInfoEntity image : value.getSuccessData()) {
                            images.add(image.getView().getFullPath());
                        }
                        getView().onLoadImageSuccess(images);
                    }

                    @Override
                    public void onFailure(String message) {
                        if (getView() == null) return;
                        getView().onLoadImageFailure(message);
                    }
                });

            }
        }).start();
    }

    public void add(String content, ArrayList<String> images) {
        AddApi api = new AddApi();
        api.contexnt = content;
        api.attachment = images;
        api.request(new ObjectRequest.OnRequestListeren<CommentEntity>() {
            @Override
            public void onSuccess(CommentEntity value) {
                if (getView() == null) return;
                getView().onAddSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onAddFailure(message);
            }
        });
    }

}
