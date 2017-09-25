package com.tiger.socol.gu.activity.product.content;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.good.GoodDetailEntity;
import com.tiger.socol.gu.api.good.GoodsCommentEntity;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private boolean margin = false;

    private List<GoodsCommentEntity> comments = new ArrayList<>();

    public CommentAdapter(boolean margin) {
        this.margin = margin;
    }

    public void setComments(List<GoodsCommentEntity> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public CommentAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (margin) {
            holder.viewFullTopLine.setVisibility(View.VISIBLE);
            holder.viewFullBottomLine.setVisibility(View.VISIBLE);
            holder.viewMarginBottomLine.setVisibility(View.GONE);
        } else {
            if (position == getItemCount() -1) {
                holder.viewMarginBottomLine.setVisibility(View.GONE);
                holder.viewFullBottomLine.setVisibility(View.VISIBLE);
            } else {
                holder.viewMarginBottomLine.setVisibility(View.VISIBLE);
                holder.viewFullBottomLine.setVisibility(View.GONE);
            }
        }
        GoodsCommentEntity entity = comments.get(position);
        holder.txCommentName.setText(entity.getNickName());
        holder.txCommentContent.setText(entity.getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txCommentName;
        TextView txCommentContent;
        View viewFullTopLine;
        View viewMarginBottomLine;
        View viewFullBottomLine;

        public ViewHolder(View itemView) {
            super(itemView);
            txCommentName = (TextView) itemView.findViewById(R.id.tx_comment_name);
            txCommentContent = (TextView) itemView.findViewById(R.id.tx_comment_content);
            viewFullTopLine =  itemView.findViewById(R.id.view_full_top_line);
            viewMarginBottomLine =  itemView.findViewById(R.id.view_margin_bottom_line);
            viewFullBottomLine =  itemView.findViewById(R.id.view_full_bottom_line);
        }
    }

}
