package com.tiger.socol.gu.activity.mine.address.list;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.address.edit.EditAddressActivity;
import com.tiger.socol.gu.activity.mine.collection.CollectionActivity;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.activity.shopCart.order.CartContentActivity;
import com.tiger.socol.gu.api.address.AddressEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.utils.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressListActivity extends BaseViewStateActivity<AddressListView, AddressListPresenter>
        implements AddressListView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listView)
    SwipeMenuListView listView;

    private AddressListAdapter adapter;
    private boolean isSelectAddress = false;

    public static final String SELECT_ADDRESS_KEY = "SELECT_ADDRESS_KEY";

    @NonNull
    @Override
    public AddressListPresenter createPresenter() {
        return new AddressListPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("常用地址");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new AddressListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressEntity entity = adapter.getList().get(i);
                if (isSelectAddress) {
                    Intent mIntent = new Intent();
                    mIntent.putExtra(IntentConstant.ADDRESS, entity);
                    setResult(CartContentActivity.ADDRESS_RESULT_CODE, mIntent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(AddressListActivity.this, EditAddressActivity.class);
                    intent.putExtra(IntentConstant.ADDRESS, entity);
                    startActivity(intent);
                }
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.RED));
                openItem.setWidth(DensityUtil.dip2px(AddressListActivity.this, 80));
                openItem.setTitle("删除");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                AddressEntity entity = adapter.getList().get(index);
                switch (index) {
                    case 0:
                        // open
                        presenter.deteleAddress(entity);
                        break;
                }
                return true;
            }
        });

        isSelectAddress = getIntent().getBooleanExtra(SELECT_ADDRESS_KEY, false);
        showProgress();
        presenter.addressList();
    }

    @OnClick(R.id.bt_add_address)
    public void onClick() {
        startActi(EditAddressActivity.class);
    }

    @Override
    public void onRequestListSuccess(List<AddressEntity> list) {
        adapter.setList(list);
        dismissProgress();
    }

    @Override
    public void onRequestListFailure(String message) {
        showToask(message);
        dismissProgress();
    }

    @Override
    public void onDeteleAddressSuccess(AddressEntity addressEntity) {
        adapter.getList().remove(addressEntity);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeteleAddressFailure(String message) {
        showToask(message);
    }

    @Subscribe
    public void onEventMainThread(AddressEvent event) {
        presenter.addressList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

