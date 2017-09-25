package com.tiger.socol.gu.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tiger.socol.gu.R;


/**
 * 我的 条目控件 tu
 */
public class MineSettingItemView extends RelativeLayout {

    private Context context;
    /**
     * 图标
     */
    private ImageView im_setting_icon;
    /**
     * 箭头
     */
    private ImageView im_setting_arrow;
    private RelativeLayout layout;
    /**
     * 描述
     */
    private TextView tx_setting_info;
    /**
     * 标题
     */
    private TextView tx_setting_title;

    public MineSettingItemView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public MineSettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
        configType(attrs);
    }

    private void configType(AttributeSet attrs) {
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.SettingItem);

        // 设置标题
        String title = types.getString(R.styleable.SettingItem_itemTitle);
        setTitle(title);

        // 设置图标
        int iconid = types.getResourceId(R.styleable.SettingItem_itemIcon, 0);
        if (iconid != 0) {
            setIconImageResource(iconid);
        }

        // 显示顶部分割线
        if (types.getBoolean(R.styleable.SettingItem_showFullTopLine, false))
            showFullTopLine();

        // 显示底部分割线
        if (types.getBoolean(R.styleable.SettingItem_showFullBottomLine, false))
            showFullButtonLine();

        // 显示底部对其分割线
        if (types.getBoolean(R.styleable.SettingItem_showMarginBottomLine, false))
            showMarginBottomLine();

        // 隐藏箭头
        if (types.getBoolean(R.styleable.SettingItem_hidArrow, false))
            hiddenArrowImage();
    }

    /**
     * 初始化View
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (RelativeLayout) inflater.inflate(R.layout.view_mine_setting_item, null);
        addView(layout);

        tx_setting_info = (TextView) layout.findViewById(R.id.tv_setting_info);
        im_setting_icon = (ImageView) layout.findViewById(R.id.iv_setting_icon);
        tx_setting_title = (TextView) layout.findViewById(R.id.tv_setting_title);
        im_setting_arrow = (ImageView) layout.findViewById(R.id.iv_setting_arrow);
    }

    /**
     * 设置Item 标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        tx_setting_title.setText(title);
    }

    /**
     * 设置Item 信息
     *
     * @param info 信息
     */
    public void setInfo(String info) {
        tx_setting_info.setText(info);
    }

    /**
     * 设置Item 图标
     *
     * @param resId 图标资源id
     */
    public void setIconImageResource(@DrawableRes int resId) {
        im_setting_icon.setImageResource(resId);
    }

    /**
     * 隐藏箭头
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void hiddenArrowImage() {
        // 设置描述文本和箭头对齐
        LayoutParams lp = (LayoutParams) tx_setting_info.getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        lp.removeRule(RelativeLayout.LEFT_OF);
        lp.addRule(RelativeLayout.ALIGN_RIGHT, im_setting_arrow.getId());

        tx_setting_info.setLayoutParams(lp);

        // 隐藏箭头图片
        im_setting_arrow.setVisibility(View.INVISIBLE);
    }

    private View newLineView() {
        int heigth = (int) context.getResources().getDimension(R.dimen.y1);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        View lineView = new View(context);
        lineView.setBackgroundColor(context.getResources().getColor(R.color.color_dddddd));
        layout.addView(lineView, lp);
        return lineView;
    }

    /***
     * 显示上分割线
     */
    public void showFullTopLine() {
        View line = newLineView();
        line.setBackgroundColor(getResources().getColor(R.color.color_line));
        LayoutParams lp = (LayoutParams) line.getLayoutParams();
        lp.height = (int) context.getResources().getDimension(R.dimen.y1);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        line.setLayoutParams(lp);
    }

    /**
     * 显示底部分割线
     */
    public void showMarginBottomLine() {
        View line = newLineView();
        line.setBackgroundColor(getResources().getColor(R.color.color_line));
        LayoutParams lp = (LayoutParams) line.getLayoutParams();
        lp.height = (int) context.getResources().getDimension(R.dimen.y1);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_LEFT, im_setting_icon.getId());
        line.setLayoutParams(lp);
    }

    /**
     * 显示底部分割线
     */
    public void showFullButtonLine() {
        View line = newLineView();
        line.setBackgroundColor(getResources().getColor(R.color.color_line));
        LayoutParams lp = (LayoutParams) line.getLayoutParams();
        lp.height = (int) context.getResources().getDimension(R.dimen.y1);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        line.setLayoutParams(lp);
    }

}