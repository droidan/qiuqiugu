package com.tiger.socol.gu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.tiger.socol.gu.activity.community.CommunityFragment;
import com.tiger.socol.gu.activity.mine.root.MineFragment;
import com.tiger.socol.gu.activity.product.ProductFragment;
import com.tiger.socol.gu.activity.shopCart.root.CartEvent;
import com.tiger.socol.gu.activity.shopCart.root.ShopCartFragment;
import com.tiger.socol.gu.utils.FileUtlis;
import com.tiger.socol.gu.views.widgets.BadgeView;
import com.tiger.socol.gu.views.widgets.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.cvp_main)
    CustomViewPager viewPager;
    @BindView(R.id.stl_main)
    SmartTabLayout viewpagerTab;

    BadgeView zhiBadge;

    private long onKeyDownClickTime;
    final int[] tabIcons = {R.drawable.selector_tab_cc, R.drawable.selector_tab_news, R.drawable.selector_tab_lesson, R.drawable.selector_tab_setting};
    final int[] tabNames = {R.string.tab_lesson, R.string.tab_shoppiing, R.string.tab_news, R.string.tab_setting};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        EventBus.getDefault().register(this);
        afterViewInit();
        FileUtlis.getIntence().initFile(this);
    }

    protected void afterViewInit() {
        FragmentPagerItems pages = FragmentPagerItems.with(this)
                .add(R.string.tab_lesson, ProductFragment.class)
                .add(R.string.tab_shoppiing, ShopCartFragment.class)
                .add(R.string.tab_news, CommunityFragment.class)
                .add(R.string.tab_setting, MineFragment.class).create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
        viewPager.setOffscreenPageLimit(pages.size());
        viewPager.setAdapter(adapter);
        viewPager.setCanScroll(false);

        final LayoutInflater inflater = LayoutInflater.from(this);
        viewpagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                View view = inflater.inflate(R.layout.view_tab_icon, container, false);
                ImageView iconView = (ImageView) view.findViewById(R.id.imageView);
                iconView.setBackgroundResource(tabIcons[position % tabIcons.length]);
                TextView textView = (TextView) view.findViewById(R.id.tab_name);
                textView.setText(tabNames[position % tabNames.length]);

                View tab = view.findViewById(R.id.tab_view);
                if (position == 1) {
                    zhiBadge = new BadgeView(MainActivity.this, tab);
                    zhiBadge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                    zhiBadge.setBadgeMargin(-1);
                    zhiBadge.setTextSize(11);
                }
                return view;
            }
        });
        viewpagerTab.setViewPager(viewPager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(CartNumEvent event) {
        if (event.getNum() == 0) {
            zhiBadge.hide();
        } else {
            zhiBadge.setText(event.getNum() + "");
            zhiBadge.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - onKeyDownClickTime < 2000) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            } else {
//                ToastUtils.show(this, R.string.press_again);
                onKeyDownClickTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
