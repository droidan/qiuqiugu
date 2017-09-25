package com.tiger.socol.gu.activity.mine.collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CollectionAdapter extends BaseAdapter {

    private List<GoodsEntity> goods = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public void delete(GoodsEntity entity) {
        goods.remove(entity);
        notifyDataSetChanged();
    }

    public CollectionAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<GoodsEntity> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsEntity> goods) {
        this.goods = goods;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_collect, null);
            holder.mrbGood = (MaterialRatingBar) convertView.findViewById(R.id.mrb_good);
            holder.txPorductName = (TextView) convertView.findViewById(R.id.tx_porduct_name);
            holder.txPorductPrice = (TextView) convertView.findViewById(R.id.tx_porduct_price);
            holder.imPorductThumb = (ImageView) convertView.findViewById(R.id.im_porduct_thumb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GoodsEntity entity = goods.get(position);
        holder.txPorductName.setText(entity.getGoodsName());
        holder.txPorductPrice.setText(entity.getPrice() + "");
        holder.mrbGood.setRating((float) entity.getStars());
        String thumbs = entity.getThumb();
        if (!StringUtils.isEmpty(thumbs)) {
            Picasso.with(context).load(thumbs).into(holder.imPorductThumb);
        }
        return convertView;
    }

    public class ViewHolder {
        MaterialRatingBar mrbGood;
        ImageView imPorductThumb;
        TextView txPorductName;
        TextView txPorductPrice;
    }

}
