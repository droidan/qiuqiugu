package com.tiger.socol.gu.activity.community;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.views.widgets.ExpandTextView;

public abstract class CircleViewHolder extends RecyclerView.ViewHolder {

    public final static int TYPE_IMAGE = 2;

    public int viewType;

    public ImageView headIv;
    public TextView nameTv;
    public TextView timeTv;
    public TextView deleteBtn;

    public TextView likeNumTv;
    public TextView commentNumTv;
    public ImageView likeStutasIv;
    public LinearLayout likeBt;

    public ExpandTextView contentTv;

    public CircleViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);
        initSubView(viewType, viewStub);
        nameTv = (TextView) itemView.findViewById(R.id.nameTv);
        likeNumTv = (TextView) itemView.findViewById(R.id.tx_like_num);
        commentNumTv = (TextView) itemView.findViewById(R.id.tx_comment_num);
        likeStutasIv = (ImageView) itemView.findViewById(R.id.im_like_status);
        likeBt = (LinearLayout) itemView.findViewById(R.id.bt_like);
        headIv = (ImageView) itemView.findViewById(R.id.headIv);
        deleteBtn = (TextView) itemView.findViewById(R.id.deleteBtn);
        timeTv = (TextView) itemView.findViewById(R.id.timeTv);
        contentTv = (ExpandTextView) itemView.findViewById(R.id.contentTv);
    }

    public abstract void initSubView(int viewType, ViewStub viewStub);

}
