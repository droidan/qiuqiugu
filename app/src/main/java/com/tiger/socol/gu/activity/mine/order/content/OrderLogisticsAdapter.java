package com.tiger.socol.gu.activity.mine.order.content;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.order.ExpressEntity;

import java.util.ArrayList;
import java.util.List;


public class OrderLogisticsAdapter extends RecyclerView.Adapter<OrderLogisticsAdapter.ViewHolder> {

    private List<ExpressEntity.ExpressListBean> expresss = new ArrayList<>();

    public void setExpresss(List<ExpressEntity.ExpressListBean> expresss) {
        this.expresss = expresss;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_order_logistics, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.viewTopLine.setVisibility(View.INVISIBLE);
            holder.imLogisticsStatus.setBackgroundResource(R.mipmap.order_tracking_max);
        } else {
            holder.imLogisticsStatus.setBackgroundResource(R.mipmap.order_tracking_min);
            holder.viewTopLine.setVisibility(View.VISIBLE);
        }

        if (position == getItemCount() - 1) {
            holder.viewBottomLine.setVisibility(View.INVISIBLE);
        } else {
            holder.viewBottomLine.setVisibility(View.VISIBLE);
        }

        ExpressEntity.ExpressListBean bean = expresss.get(position);
        holder.txLogisticsTime.setText(bean.getTime());
        holder.txLogisticsInfo.setText(bean.getContext());
    }

    @Override
    public int getItemCount() {
        return expresss.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txLogisticsInfo;
        TextView txLogisticsTime;
        View viewTopLine;
        ImageView imLogisticsStatus;
        View viewBottomLine;

        public ViewHolder(View itemView) {
            super(itemView);
            txLogisticsInfo = (TextView) itemView.findViewById(R.id.tx_logistics_info);
            txLogisticsTime = (TextView) itemView.findViewById(R.id.tx_logistics_time);
            viewTopLine = itemView.findViewById(R.id.view_top_line);
            viewBottomLine = itemView.findViewById(R.id.view_bottom_line);
            imLogisticsStatus = (ImageView) itemView.findViewById(R.id.im_logistics_status);
        }

    }

}
