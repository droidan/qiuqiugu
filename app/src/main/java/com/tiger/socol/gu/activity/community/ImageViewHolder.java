package com.tiger.socol.gu.activity.community;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.community.Content.ViewPagerActivity;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.api.bbs.LikeApi;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.network.NullRequest;
import com.tiger.socol.gu.views.widgets.MultiImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageViewHolder extends CircleViewHolder {

    public MultiImageView multiImageView;

    public ImageViewHolder(View itemView) {
        super(itemView, TYPE_IMAGE);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if (viewStub == null) {
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) subView.findViewById(R.id.multiImagView);
        if (multiImageView != null) {
            this.multiImageView = multiImageView;
        }
    }

    public void setData(final Context context, final BBSEntity entity) {
        contentTv.setText(entity.getContent());
        nameTv.setText(entity.getNickName());
        timeTv.setText(entity.getCreated());
        likeNumTv.setText(entity.getLikes() + "");
        commentNumTv.setText(entity.getComments() + "");
        if (entity.isLike()) {
            likeStutasIv.setImageDrawable(context.getResources().getDrawable(R.mipmap.praise));
        } else {
            likeStutasIv.setImageDrawable(context.getResources().getDrawable(R.mipmap.no_praise));
        }

        String avatar = entity.getAvatar();
        if (!StringUtils.isEmpty(avatar)) {
            Picasso.with(context).load(entity.getAvatar()).into(headIv);
        } else  {

        }

        List photos = new ArrayList<>();
        List<String> thumbs = entity.getAttachment().getThumb();
        for (String thumb : thumbs) {
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.url = thumb;
            photos.add(photoInfo);
        }
        multiImageView.setList(photos);
        multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ArrayList<String> thumbs = (ArrayList<String>) entity.getAttachment().getOrg();
                Intent intent = new Intent();
                intent.putExtra(ViewPagerActivity.IMAGE_URLS_KEY, thumbs);
                intent.putExtra(ViewPagerActivity.IMAGE_POSITION_KEY, position);
                intent.setClass(context, ViewPagerActivity.class);
                context.startActivity(intent);
            }
        });

        likeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MemberMannager.checkLogin(context)) {
                    return;
                }
                if (entity.isLike()) {
                    return;
                }
                likeBBS(context, entity, likeStutasIv);
            }
        });
    }

    public void likeBBS(final Context context, final BBSEntity entity, final ImageView likeImageView) {
        LikeApi api = new LikeApi();
        api.bbsId = entity.getBbsId();
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                int like = entity.getLikes();
                like++;
                entity.setLikes(like);
                entity.setLike(true);
                likeImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.praise));
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showShortToast(context, message);
            }
        });
    }

}
