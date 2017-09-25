package com.tiger.socol.gu.activity.shopCart.root;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.collection.CollectionAdapter;
import com.tiger.socol.gu.api.index.GoodsEntity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ShopCartAdapter extends BaseAdapter {

    private List<GoodsEntity> goods = new ArrayList<>();
    private Set<Integer> selectIndexs = new HashSet<Integer>();
    private OnProductItemListener productItemListener;
    private LayoutInflater mInflater;
    private Context context;

    public ShopCartAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setProductItemListener(OnProductItemListener productItemListener) {
        this.productItemListener = productItemListener;
    }

    public void setGoods(List<GoodsEntity> goods) {
        this.goods.clear();
        this.goods = goods;
        notifyDataSetChanged();
    }

    public void remove(GoodsEntity entity) {
        goods.remove(entity);
        notifyDataSetChanged();
    }

    public List<GoodsEntity> getGoods() {
        return goods;
    }

    /**
     * 全选
     */
    public void selectALL() {
        for (int i = 0; i < goods.size(); i++) {
            GoodsEntity goodsEntity = goods.get(i);
            selectIndexs.add(goodsEntity.getGoodsId());
        }
        notifyDataSetChanged();
    }

    /**
     * 取消全选
     */
    public void deselectAll() {
        selectIndexs.clear();
        notifyDataSetChanged();
    }

    public ArrayList<GoodsEntity> selectGoods() {
        ArrayList<GoodsEntity> gs = new ArrayList<>();
        for (Integer goodId : selectIndexs) {
            for (GoodsEntity good : goods) {
                if (goodId == good.getGoodsId()) {
                    gs.add(good);
                }
            }
        }
        return gs;
    }

    private void changePrice(EditText editText, TextView txPorductPrice, GoodsEntity goodsEntity, int num) {
        goodsEntity.setNum(num);
        editText.setText(num + "");
        productItemListener.onChangeNum();
        txPorductPrice.setText("￥" + goodsEntity.getTotlaPrice() + "");
    }

    public void itemViewClick(int position) {
        GoodsEntity goodsEntity = goods.get(position);
        int goodId = goodsEntity.getGoodsId();
        boolean b = selectIndexs.contains(goodId);
        if (b) {
            selectIndexs.remove(goodId);
        } else {
            selectIndexs.add(goodId);
        }
        productItemListener.onSelectAll(selectIndexs.size() == goods.size());
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
            convertView = mInflater.inflate(R.layout.adapter_cart_, null);

            holder.imPorductThumb = (SimpleDraweeView) convertView.findViewById(R.id.im_porduct_thumb);
            holder.txPorductName = (TextView) convertView.findViewById(R.id.tx_porduct_name);
            holder.txPorductPrice = (TextView) convertView.findViewById(R.id.tx_porduct_price);
            holder.buAdd = (Button) convertView.findViewById(R.id.bu_add);
            holder.btJian = (Button) convertView.findViewById(R.id.bt_jian);
            holder.editText = (EditText) convertView.findViewById(R.id.ed_product_num);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
            holder.llSelect = (LinearLayout) convertView.findViewById(R.id.ll_select);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GoodsEntity goodsEntity = goods.get(position);
        holder.editText.setText(goodsEntity.getNum() + "");
        holder.txPorductPrice.setText(goodsEntity.getTotlaPrice() + "");
        holder.txPorductName.setText(goodsEntity.getGoodsName());
        holder.cbSelect.setChecked(selectIndexs.contains(goodsEntity.getGoodsId()));
        holder.cbSelect.setOnClickListener(new SelectClictListener(position));
        holder.llSelect.setOnClickListener(new SelectClictListener(position));
        if (!StringUtils.isEmpty(goodsEntity.getThumb())) {
            Picasso.with(context).load(goodsEntity.getThumb()).into(holder.imPorductThumb);
        }

        final EditText et = holder.editText;
        final TextView pp = holder.txPorductPrice;

        holder.buAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = goodsEntity.getNum();
                num++;
                changePrice(et, pp, goodsEntity, num);
            }
        });

        holder.btJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = goodsEntity.getNum();
                if (num == 1) {
                    return;
                }
                num--;
                changePrice(et, pp, goodsEntity, num);
            }
        });

//        // 产品点击事件
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                itemViewClick(position);
//            }
//        });

        return convertView;
    }


    public class ViewHolder {
        SimpleDraweeView imPorductThumb;
        TextView txPorductName;
        TextView txPorductPrice;
        Button buAdd;
        Button btJian;
        EditText editText;
        CheckBox cbSelect;
        LinearLayout llSelect;
    }

    class SelectClictListener implements View.OnClickListener {

        private int position;

        public SelectClictListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            itemViewClick(position);
        }
    }

    public interface OnProductItemListener {

        void onSelectAll(boolean select);

        void onChangeNum();
    }

}
