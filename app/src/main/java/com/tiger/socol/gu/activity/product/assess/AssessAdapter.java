package com.tiger.socol.gu.activity.product.assess;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AssessAdapter extends RecyclerView.Adapter<AssessAdapter.ViewHodler> {

    private Context context;
    private ArrayList<GoodsEntity> goods = new ArrayList<>();

    public AssessAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<GoodsEntity> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<GoodsEntity> goods) {
        this.goods = goods;
        notifyDataSetChanged();
    }

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_assess, parent, false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {
        GoodsEntity good = goods.get(position);
        holder.txPorderTitle.setText(good.getGoodsName());
        holder.txProductStatus.setText(good.getCreated());
        holder.txProductNum.setText("共" + good.getNum() + "件商品");

        String thumb = good.getThumb();
        if (!StringUtils.isEmpty(thumb)) {
            Picasso.with(context).load(thumb).into(holder.imProductThumb);
        }
    }

    public void getComment() {
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {

        MaterialRatingBar mrbGood;
        EditText edComment;
        TextView txPorderTitle;
        TextView txProductNum;
        ImageView imProductThumb;
        TextView txProductStatus;

        public ViewHodler(View itemView) {
            super(itemView);
            mrbGood = (MaterialRatingBar) itemView.findViewById(R.id.mrb_good);
            txPorderTitle = (TextView) itemView.findViewById(R.id.tx_porder_title);
            txProductNum = (TextView) itemView.findViewById(R.id.tx_product_num);
            edComment = (EditText) itemView.findViewById(R.id.ed_comment);
            txProductStatus = (TextView) itemView.findViewById(R.id.tx_product_time);
            imProductThumb = (ImageView) itemView.findViewById(R.id.im_product_thumb);
        }

    }

}
