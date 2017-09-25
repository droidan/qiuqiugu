package com.tiger.socol.gu.activity.mine.address.edit;


import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.address.AddApi;
import com.tiger.socol.gu.api.address.AddressEntity;
import com.tiger.socol.gu.api.address.EditApi;
import com.tiger.socol.gu.network.ObjectRequest;

public class EditAddressActivityPresenter extends MvpBasePresenter<EditAddressActivityView> {

    private String checkParament(String name,
                                 String phone,
                                 String province,
                                 String address) {
        if (StringUtils.isEmpty(name)) {
            return "请输入收货联系人";
        }

        if (StringUtils.isEmpty(phone)) {
            return "请输入联系号码";
        }

        if (!RegexUtils.isMobileExact(phone)) {
            return "手机格式不对";
        }

        if (StringUtils.isEmpty(province)) {
            return "请选择所在地区";
        }

        if (StringUtils.isEmpty(address)) {
            return "请填写详细地址";
        }
        return null;
    }

    /**
     * 编辑地址
     *
     * @param addressId 地址id
     * @param name      收货人
     * @param phone     联系电话
     * @param province  省
     * @param city      市
     * @param area      县
     * @param address   详细地址
     * @param defaults  是否默认
     */
    public void editAddress(int addressId,
                            String name,
                            String phone,
                            String province,
                            String city,
                            String area,
                            String address,
                            boolean defaults) {
        String error = checkParament(name, phone, province, address);
        if (error != null) {
            getView().onEditAddressFailure(error);
            return;
        }

        EditApi api = new EditApi();
        api.addressId = addressId;
        api.name = name;
        api.phone = phone;
        api.province = province;
        api.city = city;
        api.area = area;
        api.address = address;
        api.defaults = defaults;
        api.request(new ObjectRequest.OnRequestListeren<AddressEntity>() {
            @Override
            public void onSuccess(AddressEntity value) {
                if (getView() == null) return;
                getView().onEditAddressSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onEditAddressFailure(message);
            }
        });
    }

    public void addAddress(String name,
                           String phone,
                           String province,
                           String city,
                           String area,
                           String address,
                           boolean defaults) {
        String error = checkParament(name, phone, province, address);
        if (error != null) {
            getView().onAddAddressFailure(error);
            return;
        }

        AddApi api = new AddApi();
        api.name = name;
        api.phone = phone;
        api.province = province;
        api.city = city;
        api.area = area;
        api.address = address;
        api.defaults = defaults;
        api.request(new ObjectRequest.OnRequestListeren<AddressEntity>() {
            @Override
            public void onSuccess(AddressEntity value) {
                if (getView() == null) return;
                getView().onAddAddressSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onAddAddressFailure(message);
            }
        });

    }


}
