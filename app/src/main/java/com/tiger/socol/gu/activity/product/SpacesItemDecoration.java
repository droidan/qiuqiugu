package com.tiger.socol.gu.activity.product;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space=8;

    public SpacesItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.right = 0;
            outRect.bottom = space;
            outRect.top = 0;
            outRect.left = 0;
        } else {
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;
            outRect.left = space;
        }
    }

}