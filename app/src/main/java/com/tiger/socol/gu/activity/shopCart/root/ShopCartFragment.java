package com.tiger.socol.gu.activity.shopCart.root;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.blankj.utilcode.utils.ToastUtils;
import com.tiger.socol.gu.CartNumEvent;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.collection.CollectionActivity;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.activity.shopCart.order.CartContentActivity;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.base.BaseViewStateFragment;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.utils.DensityUtil;
import com.tiger.socol.gu.utils.DoubleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopCartFragment extends BaseViewStateFragment<ShopCartView, ShopCartPresenter>
        implements ShopCartView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    SwipeMenuListView recyclerView;
    @BindView(R.id.cb_select)
    CheckBox cbSelect;
    @BindView(R.id.tx_price)
    TextView txPrice;
    @BindView(R.id.bt_submit)
    Button btSubmit;
    @BindView(R.id.rl_sc)
    RelativeLayout rlSc;
    @BindView(R.id.im_null_cart)
    ImageView imNullCart;

    private ShopCartAdapter adapter;

    @NonNull
    @Override
    public ShopCartPresenter createPresenter() {
        return new ShopCartPresenter();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(CartEvent event) {
        cbSelect.setChecked(false);
        presenter.goodsList();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void aftertView() {
        toolbar.setTitle("购物车");
        cbSelect.setChecked(false);
        adapter = new ShopCartAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.itemViewClick(i);
            }
        });

        // 产品点击事件
        adapter.setProductItemListener(new ShopCartAdapter.OnProductItemListener() {

            @Override
            public void onSelectAll(boolean select) {
                cbSelect.setChecked(select);
                reloadPrice();
            }

            @Override
            public void onChangeNum() {
                reloadPrice();
            }
        });

        // 全选按钮点击事件
        cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbSelect.isChecked()) {
                    adapter.selectALL();
                } else {
                    adapter.deselectAll();
                }
                reloadPrice();
            }
        });


        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(getContext());
                openItem.setBackground(new ColorDrawable(Color.RED));
                openItem.setWidth(DensityUtil.dip2px(getContext(), 90));
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };
        recyclerView.setMenuCreator(creator);
        recyclerView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        GoodsEntity entity = adapter.getGoods().get(position);
                        presenter.delete(entity);
                        break;
                }
                return true;
            }
        });

        presenter.goodsList();
    }

    private void reloadPrice() {
        ArrayList<GoodsEntity> goods = adapter.selectGoods();
        double price = 0;
        for (GoodsEntity good : goods) {
            double p = (double) good.getNum() * good.getPrice();
            price += p;
        }
        txPrice.setText("￥" + DoubleUtils.toPrice(price) + "");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_cart;
    }

    @Override
    protected void initView(View view) {

    }

    @OnClick(R.id.bt_submit)
    public void onClick() {
        if (!MemberMannager.checkLogin(getActivity())) {
            return;
        }

        ArrayList<GoodsEntity> goods = adapter.selectGoods();
        if (goods.size() == 0) {
            ToastUtils.showShortToast(getActivity(), "请选择产品");
            return;
        }

        Intent intent = new Intent();
        intent.setClass(getActivity(), CartContentActivity.class);
        intent.putParcelableArrayListExtra(CartContentActivity.SELECT_GOODS_KEY,
                adapter.selectGoods());
        startActivity(intent);

        txPrice.setText("");
        adapter.deselectAll();
        cbSelect.setChecked(false);
    }

    @Override
    public void onRequestCartListSuccess(List<GoodsEntity> goods) {
        adapter.setGoods(goods);
        reloadUI();
        EventBus.getDefault().post(new CartNumEvent(goods.size()));
    }

    private void reloadUI() {
        if (adapter.getGoods().size() == 0) {
            rlSc.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            imNullCart.setVisibility(View.VISIBLE);
        } else {
            rlSc.setVisibility(View.VISIBLE);
            imNullCart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestCartListFailure(String message) {
        showToask(message);
    }

    @Override
    public void onDeleteSuccess(GoodsEntity goodsEntity) {
        adapter.remove(goodsEntity);
        reloadUI();
        EventBus.getDefault().post(new CartNumEvent(adapter.getCount()));
    }

    @Override
    public void onDeleteFailure(String message) {
        showToask("删除失败");
    }

}