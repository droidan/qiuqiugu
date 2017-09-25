package com.tiger.socol.gu.activity.mine.login;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.api.menber.RegistApi;
import com.tiger.socol.gu.api.upload.UploadImageApi;
import com.tiger.socol.gu.api.upload.UploadImageEntity;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.network.ObjectRequest;

import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class PerfectInfoPresenter extends MvpBasePresenter<PerfectInfoView> {

    private Context context;

    public PerfectInfoPresenter(Context context) {
        this.context = context;
    }

    public void upload(ArrayList<String> files) {
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
                getView().onLoadImageSuccess(value.getSuccessData().get(0).getView().getFullPath());
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onLoadImageFailure(message);
            }
        });
    }

    public void regist(String phone, String password, String avatarPath, String name) {
        RegistApi api = new RegistApi();
        api.password = password;
        api.phone = phone;
        api.avatar = "";
        api.nickName = name;
        api.request(new ObjectRequest.OnRequestListeren<Member>() {
            @Override
            public void onSuccess(Member value) {
                if (getView() == null) return;
                MemberMannager.getInstance().savaMember(value);
                getView().onRegistSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRegistFailure(message);
            }
        });
    }

}
