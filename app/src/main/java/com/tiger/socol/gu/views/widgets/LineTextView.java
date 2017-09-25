package com.tiger.socol.gu.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiger.socol.gu.R;
public class LineTextView extends RelativeLayout {

    private Context context;
    private TextView title;
    private View line;

    public LineTextView(Context context) {
        super(context);
        this.context = context;
        initView();
        setSelected(false);
    }

    public LineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        setSelected(false);
    }

    public void setSelected(boolean selected) {
        int color = 0;
        if (selected) {
            color =  context.getResources().getColor(R.color.theme_color);
            line.setVisibility(View.VISIBLE);
        } else {
            color =  context.getResources().getColor(R.color.color_3e3e3e);
            line.setVisibility(View.GONE);
        }
        line.setBackgroundColor(color);
        title.setTextColor(color);
    }

    public void setText(String text) {
        title.setText(text);
    }

    public void setTextSize(float size) {
        title.setTextSize(size);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.view_line_text, null);
        addView(layout);

        title = (TextView) layout.findViewById(R.id.tx_title);
        line = layout.findViewById(R.id.view_line);
    }


}
