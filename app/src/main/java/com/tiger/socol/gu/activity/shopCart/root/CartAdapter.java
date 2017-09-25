package com.tiger.socol.gu.activity.shopCart.root;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.utils.DoubleUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<GoodsEntity> goods = new ArrayList<>();
    private Set<Integer> selectIndexs = new HashSet<Integer>();
    private Context context;

    public void setProductItemListener(OnProductItemListener productItemListener) {
        this.productItemListener = productItemListener;
    }

    private OnProductItemListener productItemListener;

    public void setGoods(List<GoodsEntity> goods) {
        this.goods.clear();
        this.goods = goods;
        notifyDataSetChanged();
    }

    public List<GoodsEntity> getGoods() {
        return goods;
    }

    public CartAdapter(Context context) {
        this.context = context;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart_, parent, false));
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
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

        holder.buAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = goodsEntity.getNum();
                num++;
                changePrice(holder, goodsEntity, num);
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
                changePrice(holder, goodsEntity, num);
            }
        });

        // 产品点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemViewClick(position);
            }
        });
    }

    private void changePrice(CartAdapter.ViewHolder holder, GoodsEntity goodsEntity, int num) {
        goodsEntity.setNum(num);
        holder.editText.setText(num + "");
        productItemListener.onChangeNum();
        holder.txPorductPrice.setText("￥"+goodsEntity.getTotlaPrice() + "");
    }

    private void itemViewClick(int position) {
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
    public int getItemCount() {
        return goods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imPorductThumb;
        TextView txPorductName;
        TextView txPorductPrice;
        Button buAdd;
        Button btJian;
        EditText editText;
        CheckBox cbSelect;
        LinearLayout llSelect;

        public ViewHolder(View view) {
            super(view);
            imPorductThumb = (SimpleDraweeView) view.findViewById(R.id.im_porduct_thumb);
            txPorductName = (TextView) view.findViewById(R.id.tx_porduct_name);
            txPorductPrice = (TextView) view.findViewById(R.id.tx_porduct_price);
            buAdd = (Button) view.findViewById(R.id.bu_add);
            btJian = (Button) view.findViewById(R.id.bt_jian);
            editText = (EditText) view.findViewById(R.id.ed_product_num);
            cbSelect = (CheckBox) view.findViewById(R.id.cb_select);
            llSelect = (LinearLayout) view.findViewById(R.id.ll_select);
        }

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

        void onItemSelectClick(int position);

        void onSelectAll(boolean select);

        void onChangeNum();
    }

}