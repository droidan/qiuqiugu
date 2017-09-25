package com.tiger.socol.gu.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.models.SliderItem;
import com.tiger.socol.gu.views.widgets.slideview.BaseSliderView;
import com.tiger.socol.gu.views.widgets.slideview.SliderLayout;
import com.tiger.socol.gu.views.widgets.slideview.TextSliderView;

import java.util.List;


/**
 * 资讯列表的幻灯片头部
 * Created by jian_zhou on 2016/8/24.
 */
public class SliderPagerView extends RelativeLayout implements BaseSliderView.OnSliderClickListener {

    private Context context;
    private SliderLayout sliderView;

    public SliderPagerView(Context context) {
        super(context);
        initView(context);
    }

    public SliderPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SliderPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        init(context);
        View headerView = LayoutInflater.from(context).inflate(R.layout.view_newsheader, this);
        sliderView = (SliderLayout) headerView.findViewById(R.id.news_header_slider);
        sliderView.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderView.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
    }

    private void init(Context context) {
        this.context = context;
    }

    public void setScrollerDuration(int duration) {
        sliderView.setScrollerDuration(duration);
    }

    public void setSlderData(List<SliderItem> slides) {
        if (slides == null || slides.size() == 0) {
            this.removeAllViews();
            return;
        }

       /* sliderView.setScrollble(slides.size() != 1);*/

        sliderView.removeAllSliders();
        sliderView.setJustOne(slides.size() == 1);
        for (int i = 0; i < slides.size(); i++) {
            TextSliderView textSliderView = new TextSliderView(context);
            textSliderView.setOnSliderClickListener(this);
            textSliderView.description(slides.get(i).title).image(slides.get(i).thumb);
            textSliderView.descriptionisVisible(isvisible);
            textSliderView.getBundle().putSerializable("slide", slides.get(i));
            sliderView.addSlider(textSliderView);
        }
        sliderView.notifyData();
        sliderView.setDefaultIndicatorColor(Color.parseColor("#999999"), Color.parseColor("#d0d0d1"));
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    private boolean isvisible = true;

    public void setDescriptionisVisible(boolean isvisible) {
        this.isvisible = isvisible;
    }


    public void setContextcc() {
        sliderView.setDefaultIndicatorColor(Color.parseColor("#52bff1"), Color.parseColor("#df6d44"));
    }
}