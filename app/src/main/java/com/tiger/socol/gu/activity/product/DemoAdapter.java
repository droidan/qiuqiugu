package com.tiger.socol.gu.activity.product;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
import com.tiger.socol.gu.activity.community.HeadViewHolder;
import com.tiger.socol.gu.activity.product.content.FarmContentActivity;
import com.tiger.socol.gu.activity.product.content.ProductContentActivity;
import com.tiger.socol.gu.activity.shopCart.root.CartEvent;
import com.tiger.socol.gu.api.cart.AddApi;
import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.api.good.CollectApi;
import com.tiger.socol.gu.api.index.FarmEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.network.NullRequest;
import com.tiger.socol.gu.network.ObjectRequest;
import com.tiger.socol.gu.views.widgets.slideview.BaseSliderView;
import com.tiger.socol.gu.views.widgets.slideview.SliderLayout;
import com.tiger.socol.gu.views.widgets.slideview.TextSliderView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private boolean showSlider;
    private double itemViewWidth = -1;

    private List<FarmEntity> farms = new ArrayList<>();
    private List<GoodsEntity> goods = new ArrayList<>();

    public DemoAdapter(Activity activity, boolean showSlider) {
        this.activity = activity;
        this.showSlider = showSlider;
    }

    public void setGoods(List<GoodsEntity> goods) {
        this.goods = goods;
        notifyDataSetChanged();
    }

    public void setFarms(List<FarmEntity> farms) {
        this.farms = farms;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head, parent, false));
        }
        return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_one, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            if (goods.size() == 0) return;
            final DemoAdapter.OneViewHolder h = (OneViewHolder) holder;
            final GoodsEntity entity = goods.get(position - (showSlider ? 1 : 0));
            h.txProductName.setText(entity.getGoodsName());
            h.txProductPrice.setText("￥" + entity.getPrice() + "");
            if (!StringUtils.isEmpty(entity.getThumb())) {
                Picasso.with(activity).load(entity.getThumb()).into(h.imProduceThumb);
            }

            h.itemView.post(new Runnable() {
                @Override
                public void run() {
                    if (itemViewWidth == -1) {
                        itemViewWidth = h.itemView.getWidth();
                    }
                    ViewGroup.LayoutParams params = h.imProduceThumb.getLayoutParams();
                    double b = itemViewWidth / entity.getWidth();
                    int height = (int) (entity.getHeight() * b);
                    params.width = (int) itemViewWidth;
                    params.height = height;
                    Log.e("height", "oldWidth = " + itemViewWidth + "， entity.getWidth() = " + entity.getWidth() + ", b = " + b + ", height = " + height);
                    h.imProduceThumb.setLayoutParams(params);
                }
            });


            // 是否收藏
            if (entity.isCollected()) {
                h.btProductCollect.setBackgroundResource(R.mipmap.collect);
            } else {
                h.btProductCollect.setBackgroundResource(R.mipmap.no_collect);
            }

//            // 是否添加到购物车
//            if (entity.isAddToCar()) {
//                h.btProductBuy.setBackgroundResource(R.mipmap.add_to_cart);
//            } else {
//                h.btProductBuy.setBackgroundResource(R.mipmap.not_add_to_cart);
//            }

            // 购买按钮
            h.btProductBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!MemberMannager.checkLogin(activity)) {
                        return;
                    }
//                    if (entity.isAddToCar()) {
//                        ToastUtils.showShortToast(activity, "已添加过");
//                    } else {
                        addCart(entity, h);
//                    }
                }
            });

            // 收藏按钮
            h.btProductCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!MemberMannager.checkLogin(activity)) {
                        return;
                    }
                    collectGood(entity, h);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(activity, ProductContentActivity.class);
                    intent.putExtra(ProductContentActivity.PRODUCT_ID_KEY, entity.getGoodsId());
                    activity.startActivity(intent);
                }
            });
        } else {
            // 这是宽度为全屏
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);

            Point point = new Point();
            activity.getWindowManager().getDefaultDisplay().getRealSize(point);
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.width = point.x;
            int height = params.width / 16 * 9;
            params.height = height;
            holder.itemView.setLayoutParams(params);

            HeadViewHolder h = (HeadViewHolder) holder;
            h.sliderLayout.clean();
            for (FarmEntity farm : farms) {
                TextSliderView textSliderView = new TextSliderView(activity);
                textSliderView.image(farm.getThumb());
                textSliderView.getBundle().putSerializable(IntentConstant.FARM_ID, farm.getBaseId());
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        int farmId = slider.getBundle().getInt(IntentConstant.FARM_ID);
                        Intent intent = new Intent();
                        intent.setClass(activity, FarmContentActivity.class);
                        intent.putExtra(FarmContentActivity.FARM_CONTENT_ID_KEY, farmId);
                        activity.startActivity(intent);
                    }
                });
                h.sliderLayout.addSlider(textSliderView);
            }
            h.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return showSlider ? 0 : 1;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return goods.size() + (showSlider ? 1 : 0);
    }

    public void collectGood(final GoodsEntity entity, final DemoAdapter.OneViewHolder h) {
        CollectApi api = new CollectApi();
        api.goodsId = entity.getGoodsId();
        api.collect = !entity.isCollected();
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                entity.setCollected(!entity.isCollected());
                // 是否收藏
                if (entity.isCollected()) {
                    h.btProductCollect.setBackgroundResource(R.mipmap.collect);
                } else {
                    h.btProductCollect.setBackgroundResource(R.mipmap.no_collect);
                }
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showShortToast(activity, "操作失败");
            }
        });
    }

    public void addCart(final GoodsEntity entity, final DemoAdapter.OneViewHolder h) {
        AddApi api = new AddApi();
        api.goodsId = entity.getGoodsId();
        api.request(new ObjectRequest.OnRequestListeren<MsgEntity>() {
            @Override
            public void onSuccess(MsgEntity value) {
                entity.setAddToCar(true);
                EventBus.getDefault().post(new CartEvent());
                ToastUtils.showShortToast(activity, "添加成功");
                // 是否添加到购物车
//                if (entity.isAddToCar()) {
//                    h.btProductBuy.setBackgroundResource(R.mipmap.add_to_cart);
//                } else {
//                    h.btProductBuy.setBackgroundResource(R.mipmap.not_add_to_cart);
//                }
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showShortToast(activity, "添加失败");
            }
        });
    }

    public class OneViewHolder extends RecyclerView.ViewHolder {
        private ImageView imProduceThumb;
        private TextView txProductName;
        private TextView txProductPrice;
        private Button btProductBuy;
        private Button btProductCollect;

        public OneViewHolder(View view) {
            super(view);
            txProductName = (TextView) view.findViewById(R.id.tx_product_name);
            txProductPrice = (TextView) view.findViewById(R.id.tx_product_price);
            imProduceThumb = (ImageView) view.findViewById(R.id.im_product_thumb);
            btProductBuy = (Button) view.findViewById(R.id.bt_product_buy);
            btProductCollect = (Button) view.findViewById(R.id.bt_product_collect);
        }
    }



}
