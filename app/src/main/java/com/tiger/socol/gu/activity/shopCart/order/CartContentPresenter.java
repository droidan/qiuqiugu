package com.tiger.socol.gu.activity.shopCart.order;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.api.address.AddressEntity;
import com.tiger.socol.gu.api.address.ListApi;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.api.menber.VoucherEntity;
import com.tiger.socol.gu.api.menber.VoucherListApi;
import com.tiger.socol.gu.api.order.ExpressCostEntity;
import com.tiger.socol.gu.api.order.OrderEntity;
import com.tiger.socol.gu.api.order.PostageApi;
import com.tiger.socol.gu.api.order.SubmitApi;
import com.tiger.socol.gu.network.ArrayRequest;
import com.tiger.socol.gu.network.ObjectRequest;

import java.util.List;

public class CartContentPresenter extends MvpBasePresenter<CartContentView> {

    /**
     * 结算
     *
     * @param addressId    地址id
     * @param goods        商品
     * @param payMode      在线支付”online” 货到付款”delivery”
     * @param voucher      礼券码
     * @param totalPrice   交易总金额
     * @param goodsPrice   商品总价
     * @param expressPrice 快递费用
     */
    public void submit(int addressId,
                       List<GoodsEntity> goods,
                       String payMode,
                       String voucher,
                       double totalPrice,
                       double goodsPrice,
                       double expressPrice) {
        SubmitApi api = new SubmitApi();
        api.addressId = addressId;
        api.goods = goodParament(goods);
        api.payMode = payMode;
        api.voucher = voucher;
        api.totalPrice = totalPrice;
        api.goodsPrice = goodsPrice;
        api.expressPrice = expressPrice;
        api.request(new ObjectRequest.OnRequestListeren<OrderEntity>() {
            @Override
            public void onSuccess(OrderEntity value) {
                if (getView() == null) return;
                getView().orderPaySuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().orderPayFailure(message);
            }
        });
    }

    private String goodParament(List<GoodsEntity> goods) {
        StringBuffer sb = new StringBuffer();
        for (GoodsEntity good : goods) {
            sb.append(good.getGoodsId() + "");
            sb.append(":");
            sb.append(good.getNum() + "");
            sb.append(",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    public void requestAddress() {
        ListApi api = new ListApi();
        api.request(new ArrayRequest.OnRequestListeren<AddressEntity>() {
            @Override
            public void onSuccess(List<AddressEntity> value) {
                if (getView() == null) return;
                for (AddressEntity address : value) {
                    if (address.isDefaultX()) {
                        getView().onRequestAddressSuccess(address);
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestAddressFailure(message);
            }
        });

    }

    public void requestVoucherList() {
        VoucherListApi api = new VoucherListApi();
        api.request(new ArrayRequest.OnRequestListeren<VoucherEntity>() {
            @Override
            public void onSuccess(List<VoucherEntity> value) {
                if (getView() == null) return;
                getView().onRequestVoucherListSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestVoucherListFailure(message);
            }
        });
    }

    public void requestPostage() {
        PostageApi api = new PostageApi();
        api.request(new ObjectRequest.OnRequestListeren<ExpressCostEntity>() {
            @Override
            public void onSuccess(ExpressCostEntity value) {
                if (getView() == null) return;
                getView().onRequestPostageSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestPostageFailure(message);
            }
        });
    }

}
