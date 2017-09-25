package com.tiger.socol.gu.activity.shopCart.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.ArrayList;


public class CartContentAdapter extends RecyclerView.Adapter<CartContentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GoodsEntity> goods;

    public CartContentAdapter(Context context, ArrayList<GoodsEntity> goods) {
        this.context = context;
        this.goods = goods;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart_content, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GoodsEntity goodsEntity = goods.get(position);
        holder.txPorductNum.setText(goodsEntity.getNum() + "");
        holder.txPorductName.setText(goodsEntity.getGoodsName());
        holder.txPorductPrice.setText("￥"+goodsEntity.getTotlaPrice() + "");
        if(!StringUtils.isEmpty(goodsEntity.getThumb())) {
            Picasso.with(context).load(goodsEntity.getThumb()).into(holder.imPorductThumb);
        }
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imPorductThumb;
        TextView txPorductName;
        TextView txPorductPrice;
        TextView txPorductNum;

        public ViewHolder(View view) {
            super(view);
            imPorductThumb = (ImageView) view.findViewById(R.id.im_porduct_thumb);
            txPorductName = (TextView) view.findViewById(R.id.tx_porduct_name);
            txPorductPrice = (TextView) view.findViewById(R.id.tx_porduct_price);
            txPorductNum = (TextView) view.findViewById(R.id.tx_porduct_num);
        }
    }

}