package com.tiger.socol.gu.activity.community;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.views.widgets.slideview.SliderLayout;

public class HeadViewHolder extends RecyclerView.ViewHolder {

    public SliderLayout sliderLayout;

    public HeadViewHolder(View view) {
        super(view);
        sliderLayout = (SliderLayout) view.findViewById(R.id.sd_farm);
    }

}