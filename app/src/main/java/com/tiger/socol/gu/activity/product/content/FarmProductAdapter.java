package com.tiger.socol.gu.activity.product.content;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FarmProductAdapter extends RecyclerView.Adapter<FarmProductAdapter.ViewHolder> {


    private Activity activity;

    private List<GoodsEntity> goodList = new ArrayList<>();

    public void setGoodList(List<GoodsEntity> goodList) {
        this.goodList = goodList;
        notifyDataSetChanged();
    }

    public FarmProductAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_farm_product, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GoodsEntity entity = goodList.get(position);
        holder.txPorductName.setText(entity.getGoodsName());
        if (!StringUtils.isEmpty(entity.getThumb())) {
            Picasso.with(activity).load(entity.getThumb()).into(holder.imProductThumb);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(activity, ProductContentActivity.class);
                intent.putExtra(ProductContentActivity.PRODUCT_ID_KEY,entity.getGoodsId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txPorductName;
        ImageView imProductThumb;
        public ViewHolder(View itemView) {
            super(itemView);
            txPorductName = (TextView) itemView.findViewById(R.id.tx_porduct_name);
            imProductThumb = (ImageView) itemView.findViewById(R.id.im_product_thumb);
        }
    }
}
