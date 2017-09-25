package com.tiger.socol.gu.activity.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.community.Content.CommentDialog;
import com.tiger.socol.gu.activity.community.Content.CommunityContentActivity;
import com.tiger.socol.gu.activity.product.DemoAdapter;
import com.tiger.socol.gu.activity.product.content.FarmContentActivity;
import com.tiger.socol.gu.activity.product.content.LiveActivity;
import com.tiger.socol.gu.activity.product.content.ProductContentActivity;
import com.tiger.socol.gu.api.bbs.ADVApi;
import com.tiger.socol.gu.api.bbs.AdEntity;
import com.tiger.socol.gu.api.bbs.AddCommentApi;
import com.tiger.socol.gu.api.bbs.BBSEntity;
import com.tiger.socol.gu.api.bbs.CommentEntity;
import com.tiger.socol.gu.api.index.FarmEntity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.network.ObjectRequest;
import com.tiger.socol.gu.utils.ToastUtils;
import com.tiger.socol.gu.views.widgets.slideview.BaseSliderView;
import com.tiger.socol.gu.views.widgets.slideview.SliderLayout;
import com.tiger.socol.gu.views.widgets.slideview.TextSliderView;

import java.util.ArrayList;
import java.util.List;

public class CircleAdapter extends BaseRecycleViewAdapter {

    private Activity activity;
    private List<BBSEntity> lists = new ArrayList<>();
    private List<AdEntity> ads = new ArrayList<>();

    public CircleAdapter(Activity context) {
        this.activity = context;
    }

    public void setBBSList(List<BBSEntity> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    public void setAds(List<AdEntity> ads) {
        this.ads = ads;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (ads.size() > 0) {
            if (position == 0) {
                return 1;
            }
            return CircleViewHolder.TYPE_IMAGE;
        } else {
            return CircleViewHolder.TYPE_IMAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head, parent, false));
        } else {
            RecyclerView.ViewHolder viewHolder = null;
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);
            viewHolder = new ImageViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        int type = getItemViewType(position);
        if (type == 1) {
            Point point = new Point();
            activity.getWindowManager().getDefaultDisplay().getRealSize(point);
            ViewGroup.LayoutParams params = viewHolder.itemView.getLayoutParams();
            params.width = point.x;
            int height = params.width / 3;
            params.height = height;
            viewHolder.itemView.setLayoutParams(params);

            HeadViewHolder h = (HeadViewHolder) viewHolder;
            h.sliderLayout.clean();
            for (AdEntity adEntity : ads) {
                TextSliderView textSliderView = new TextSliderView(activity);
                textSliderView.image(adEntity.getThumb());
                textSliderView.getBundle().putParcelable("ad", adEntity);
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        AdEntity ad = slider.getBundle().getParcelable("ad");
                        if (ad.getType().equals("link")) {
                            Intent intent = new Intent();
                            intent.setClass(activity, LiveActivity.class);
                            intent.putExtra(LiveActivity.TITLE, "详情");
                            intent.putExtra(LiveActivity.LIVE_URL_KEY, ad.getValue());
                            activity.startActivity(intent);
                        } else if (ad.getType().equals("product")) {
                            Intent intent = new Intent();
                            intent.setClass(activity, ProductContentActivity.class);
                            intent.putExtra(ProductContentActivity.PRODUCT_ID_KEY, ad.getValue());
                            activity.startActivity(intent);
                        } else if (ad.getType().equals("base")) {
                            Intent intent = new Intent();
                            intent.setClass(activity, FarmContentActivity.class);
                            intent.putExtra(FarmContentActivity.FARM_CONTENT_ID_KEY, ad.getValue());
                            activity.startActivity(intent);
                        }
                    }
                });
                h.sliderLayout.addSlider(textSliderView);
            }
            h.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        } else {
            if (lists.size() == 0) return;
            final ImageViewHolder holder = (ImageViewHolder) viewHolder;
            final BBSEntity entity = lists.get(position - ads.size() > 0 ? 1 : 0);
            holder.setData(activity, entity);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BBSEntity entity = lists.get(position);
                    Intent intent = new Intent();
                    intent.putExtra(CommunityContentActivity.BBS_ENTITY_KEY, entity);
                    intent.setClass(activity, CommunityContentActivity.class);
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lists.size() + 1;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }


}
