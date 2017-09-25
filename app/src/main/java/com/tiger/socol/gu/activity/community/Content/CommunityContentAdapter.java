package com.tiger.socol.gu.activity.community.Content;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.community.ImageViewHolder;
import com.tiger.socol.gu.activity.community.PhotoInfo;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.views.widgets.MultiImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CommunityContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private BBSEntity entity;
    private Context context;
    private List<CommentEntity> commentEntityList = new ArrayList<>();
    private OnCommentItemReplyListener replyListener;

    public void setReplyListener(OnCommentItemReplyListener replyListener) {
        this.replyListener = replyListener;
    }

    public void installComment(CommentEntity commentEntity) {
        commentEntityList.add(0, commentEntity);
        notifyDataSetChanged();
    }

    public void setCommentEntityList(List<CommentEntity> commentEntityList) {
        this.commentEntityList = commentEntityList;
        notifyDataSetChanged();
    }

    public CommunityContentAdapter(Context context) {
        this.context = context;
    }

    public void setEntity(BBSEntity entity) {
        this.entity = entity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);
            viewHolder = new ImageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_community_comment, parent, false);
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == 0) {
            ImageViewHolder holder = (ImageViewHolder) viewHolder;
            holder.setData(context, entity);
        } else {
            final CommentEntity commentEntity = commentEntityList.get(position - 1);
            ViewHolder holder = (ViewHolder) viewHolder;

            holder.txCommentTime.setText(commentEntity.getCreated());
            if (!StringUtils.isEmpty(commentEntity.getAvatar())) {
                Picasso.with(context).load(commentEntity.getAvatar()).into(holder.imUserAvatar);
            }
            holder.btReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replyListener.onReplyCleck(commentEntity.getCommentId());
                }
            });

            if (StringUtils.isEmpty(commentEntity.getReply().getContent())) {
                holder.txCommentX.setVisibility(View.GONE);
                holder.txCommentUser.setVisibility(View.GONE);
                holder.txUserName.setText(commentEntity.getNickName());
                holder.txCommentContent.setText(commentEntity.getText());
            } else {
                holder.txCommentX.setVisibility(View.VISIBLE);
                holder.txCommentUser.setVisibility(View.VISIBLE);
                holder.txCommentUser.setText(commentEntity.getNickName());
                holder.txUserName.setText(commentEntity.getReply().getNickName());
                holder.txCommentContent.setText(commentEntity.getReply().getContent());
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentEntityList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imUserAvatar;
        TextView txUserName;
        TextView txCommentContent;
        TextView txCommentUser;
        TextView txCommentX;
        TextView txCommentTime;
        Button btReply;

        public ViewHolder(View itemView) {
            super(itemView);
            imUserAvatar = (ImageView) itemView.findViewById(R.id.im_user_avatar);
            txUserName = (TextView) itemView.findViewById(R.id.tx_user_name);
            txCommentContent = (TextView) itemView.findViewById(R.id.tx_comment_content);
            txCommentUser = (TextView) itemView.findViewById(R.id.tx_comment_user);
            txCommentX = (TextView) itemView.findViewById(R.id.tx_comment_x);
            txCommentTime = (TextView) itemView.findViewById(R.id.tx_comment_time);
            btReply = (Button) itemView.findViewById(R.id.bt_reply);
        }
    }

    public interface OnCommentItemReplyListener {

        void onReplyCleck(int commentId);

    }

}
