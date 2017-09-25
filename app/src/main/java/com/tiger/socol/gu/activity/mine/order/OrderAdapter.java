package com.tiger.socol.gu.activity.mine.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.order.content.OrderContentActivity;
import com.tiger.socol.gu.activity.mine.order.content.RefundActivity;
import com.tiger.socol.gu.activity.product.assess.AssessActivity;
import com.tiger.socol.gu.activity.shopCart.order.CartContentActivity;
import com.tiger.socol.gu.activity.shopCart.paySelect.PaySelectActivity;
import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.api.order.ConfirmApi;
import com.tiger.socol.gu.api.order.OrderListEntity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.network.ObjectRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemHolder> {

    Activity activity;
    private int type = 0;
    List<OrderListEntity> orders = new ArrayList<>();

    public void setOrders(List<OrderListEntity> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public OrderAdapter(int type, Activity activity) {
        this.type = type;
        this.activity = activity;
    }

    @Override
    public OrderItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_order_product, parent, false);
        return new OrderItemHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderItemHolder holder, int position) {
        final OrderListEntity entity = orders.get(position);
        GoodsEntity good = entity.getGoods().get(0);
        holder.txPorderTitle.setText(good.getGoodsName());
        holder.txPorderPrice.setText(entity.getTotalPrice() + "");
        holder.txOrderTime.setText(entity.getCreated());
        holder.txProductNum.setText("共" + entity.getGoods().size() + "件商品");
        if (!StringUtils.isEmpty(good.getThumb())) {
            Picasso.with(activity).load(good.getThumb()).into(holder.imProductThumb);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(activity, OrderContentActivity.class);
                intent.putExtra(OrderContentActivity.TYPE, type);
                intent.putExtra(IntentConstant.SN, entity.getSn());
                intent.putExtra(IntentConstant.ORDER_ID, Integer.valueOf(entity.getOrderId()));
                activity.startActivity(intent);
            }
        });

        holder.btBuyAgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (type) {
                    case 0:
                        // 去支付
                        intent = new Intent();
                        intent.setClass(activity, PaySelectActivity.class);
                        intent.putExtra(IntentConstant.SN, entity.getSn());
                        activity.startActivity(intent);
                        break;

                    case 1:
                        // 再来一单
                        buyGoods(entity);
                        break;

                    case 2:
                        // 收货
                        confirmOrder(entity);
                        break;

                    case 3:
                        // 再来一单
                        buyGoods(entity);
                        break;

                    case 4:
                        break;
                }
            }
        });

        holder.btMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (type) {
                    case 1:
                        // 再来一单
                        buyGoods(entity);
                        break;

                    case 2:
                        // 查看物流
                        intent = new Intent();
                        intent.setClass(activity, OrderContentActivity.class);
                        intent.putExtra(OrderContentActivity.TYPE, type);
                        intent.putExtra(OrderContentActivity.PAGE_INDEX, 1);
                        intent.putExtra(IntentConstant.SN, entity.getSn());
                        intent.putExtra(IntentConstant.ORDER_ID, Integer.valueOf(entity.getOrderId()));
                        activity.startActivity(intent);
                        break;

                    case 3:
                        // 去评价
                        gotoComment(entity);
                        break;

                    case 4:
                        // 退款
                        intent = new Intent();
                        intent.setClass(activity, RefundActivity.class);
                        intent.putExtra(IntentConstant.SN, entity.getSn());
                        intent.putExtra(IntentConstant.ORDER_ID, Integer.valueOf(entity.getOrderId()));
                        activity.startActivity(intent);
                        break;
                }
            }
        });
    }

    private void gotoComment(OrderListEntity entity) {
        // 去评价
        ArrayList<GoodsEntity> goods = (ArrayList<GoodsEntity>) entity.getGoods();
        Intent intent = new Intent();
        intent.setClass(activity, AssessActivity.class);
        intent.putExtra(IntentConstant.GOODS, goods);
        intent.putExtra(IntentConstant.ORDER_ID, Integer.valueOf(entity.getOrderId()));
        activity.startActivity(intent);
    }

    /**
     * 再来一单
     *
     * @param entity
     */
    public void buyGoods(OrderListEntity entity) {
        Intent intent = new Intent();
        intent.setClass(activity, CartContentActivity.class);
        intent.putParcelableArrayListExtra(CartContentActivity.SELECT_GOODS_KEY, (ArrayList<? extends Parcelable>) entity.getGoods());
        activity.startActivity(intent);
    }

    public void confirmOrder(final OrderListEntity entity) {
        ConfirmApi api = new ConfirmApi();
        api.orderId = entity.getOrderId();
        api.sn = entity.getSn();
        api.request(new ObjectRequest.OnRequestListeren<MsgEntity>() {
            @Override
            public void onSuccess(MsgEntity value) {
                notifyDataSetChanged();
                gotoComment(entity);
                EventBus.getDefault().post(new OrderEvent(2));
                EventBus.getDefault().post(new OrderEvent(3));
                ToastUtils.showShortToast(activity, value.getMsg());
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showShortToast(activity, message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderItemHolder extends RecyclerView.ViewHolder {
        TextView txPorderPrice;
        TextView txPorderTitle;
        TextView txProductNum;
        ImageView imProductThumb;
        TextView txProductStatus;
        TextView txOrderTime;
        Button btBuyAgin;
        Button btMoreMenu;

        public OrderItemHolder(View itemView) {
            super(itemView);
            btBuyAgin = (Button) itemView.findViewById(R.id.bt_buy_agin);
            btMoreMenu = (Button) itemView.findViewById(R.id.bt_more_menu);
            txOrderTime = (TextView) itemView.findViewById(R.id.tx_order_time);
            txProductNum = (TextView) itemView.findViewById(R.id.tx_product_num);
            txPorderPrice = (TextView) itemView.findViewById(R.id.tx_porder_price);
            txPorderTitle = (TextView) itemView.findViewById(R.id.tx_porder_title);
            imProductThumb = (ImageView) itemView.findViewById(R.id.im_product_thumb);
            txProductStatus = (TextView) itemView.findViewById(R.id.tx_product_status);

            switch (type) {
                case 0:
                    btBuyAgin.setText("去支付");
                    txProductStatus.setText("等待支付");
                    btMoreMenu.setVisibility(View.GONE);
                    break;

                case 1:
                    btBuyAgin.setText("再来一单");
                    txProductStatus.setText("等待到达");
                    btMoreMenu.setVisibility(View.GONE);
                    break;

                case 2:
                    btBuyAgin.setText("确认收货");
                    btMoreMenu.setText("查看物流");
                    txProductStatus.setText("等待收货");
                    btMoreMenu.setVisibility(View.VISIBLE);
                    break;

                case 3:
                    btMoreMenu.setText("去评价");
                    btBuyAgin.setText("再来一单");
                    txProductStatus.setText("已经完成");
                    btMoreMenu.setVisibility(View.VISIBLE);
                    break;

                case 4:
                    btBuyAgin.setText("再来一单");
                    btMoreMenu.setText("查看退款");
                    txProductStatus.setText("已经退款");
                    btMoreMenu.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

}
