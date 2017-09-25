package com.tiger.socol.gu.activity.mine.address.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.address.edit.city.ScrollerNumberPicker;
import com.tiger.socol.gu.activity.mine.address.list.AddressEvent;
import com.tiger.socol.gu.api.address.AddressEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAddressActivity extends BaseViewStateActivity<EditAddressActivityView, EditAddressActivityPresenter>
        implements EditAddressActivityView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tx_address)
    TextView txAddress;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.ed_address)
    EditText edAddress;
    @BindView(R.id.sw_defult)
    Switch swDefult;
    @BindView(R.id.rl_defult)
    RelativeLayout rlDefult;

    private AddressEntity address;
    private String province;
    private String city;
    private String area;

    @NonNull
    @Override
    public EditAddressActivityPresenter createPresenter() {
        return new EditAddressActivityPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_address;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void afterViewInit() {
        if (isEditAddress()) {
            toolbar.setTitle("修改地址");
//            rlDefult.setVisibility(View.GONE);
        } else {
            toolbar.setTitle("新建地址");
//            rlDefult.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showProgress();
                if (item.getItemId() == R.id.menu_bt_submit) {
                    String error = checkInfo();
                    if (error == null) {
                        String name = edName.getText().toString();
                        String phone = edPhone.getText().toString();
                        String ad = edAddress.getText().toString();
                        if (isEditAddress()) {
                            presenter.editAddress(address.getAddressId(), name, phone, province, city, area, ad, swDefult.isChecked());
                        } else {
                            presenter.addAddress(name, phone, province, city, area, ad, swDefult.isChecked());
                        }
                    } else {
                        dismissProgress();
                        showToask(error);
                    }
                }
                return true;
            }
        });

        address = getIntent().getParcelableExtra(IntentConstant.ADDRESS);
        if (isEditAddress()) {
            province = address.getProvince();
            city = address.getCity();
            area = address.getArea();

            txAddress.setText(province + city + area);
            edName.setText(address.getName());
            edPhone.setText(address.getPhone());
            edAddress.setText(address.getAddress());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    private boolean isEditAddress() {
        return address != null;
    }

    @OnClick(R.id.ll_select_city)
    public void onClick() {
        showCityDlalog();
    }

    private void showCityDlalog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.addressdialog, null);
        builder.setView(view);
        LinearLayout addressdialog_linearlayout = (LinearLayout) view.findViewById(R.id.addressdialog_linearlayout);
        final ScrollerNumberPicker provincePicker = (ScrollerNumberPicker) view.findViewById(R.id.province);
        final ScrollerNumberPicker cityPicker = (ScrollerNumberPicker) view.findViewById(R.id.city);
        final ScrollerNumberPicker counyPicker = (ScrollerNumberPicker) view.findViewById(R.id.couny);
        final AlertDialog dialog = builder.show();
        addressdialog_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                province = provincePicker.getSelectedText();
                city = cityPicker.getSelectedText();
                area = counyPicker.getSelectedText();
                txAddress.setText(province + city + area);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onEditAddressSuccess(AddressEntity address) {
        dismissProgress();
        showToask("修改成功");
        EventBus.getDefault().post(new AddressEvent());
        finish();
    }

    @Override
    public void onEditAddressFailure(String message) {
        dismissProgress();
        showToask(message);
    }

    @Override
    public void onAddAddressSuccess(AddressEntity address) {
        showToask("添加成功");
        EventBus.getDefault().post(new AddressEvent());
        finish();
    }

    @Override
    public void onAddAddressFailure(String message) {
        dismissProgress();
        showToask(message);
    }

    private String checkInfo() {
        String name = edName.getText().toString();
        if (StringUtils.isEmpty(name)) {
            return "请输入收货人";
        }

        String phone = edPhone.getText().toString();
        if (StringUtils.isEmpty(phone)) {
            return "请输入联系方式";
        }

        String address = edAddress.getText().toString();
        if (StringUtils.isEmpty(address)) {
            return "请输入详细地址";
        }

        if (StringUtils.isEmpty(province)) {
            return "请选择地址";
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

