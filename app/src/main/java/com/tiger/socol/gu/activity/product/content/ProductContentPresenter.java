package com.tiger.socol.gu.activity.product.content;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.blankj.utilcode.utils.PhoneUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.shopCart.root.CartEvent;
import com.tiger.socol.gu.api.cart.AddApi;
import com.tiger.socol.gu.api.cart.MsgEntity;
import com.tiger.socol.gu.api.good.CollectApi;
import com.tiger.socol.gu.api.good.DetailApi;
import com.tiger.socol.gu.api.good.GoodDetailEntity;
import com.tiger.socol.gu.constant.Constant;
import com.tiger.socol.gu.network.NullRequest;
import com.tiger.socol.gu.network.ObjectRequest;

import org.greenrobot.eventbus.EventBus;

public class ProductContentPresenter extends MvpBasePresenter<ProductContentView> {

    public void requestContent(int goodsId) {
        DetailApi api = new DetailApi();
        api.goodsId = goodsId;
        api.request(new ObjectRequest.OnRequestListeren<GoodDetailEntity>() {
            @Override
            public void onSuccess(GoodDetailEntity value) {
                if (getView() == null) return;
                getView().onRequestContentSuccess(value);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onRequestContentFailure(message);
            }
        });
    }

    public void collectGood(int goodId, final boolean collect) {
        CollectApi api = new CollectApi();
        api.collect = collect;
        api.goodsId = goodId;
        api.request(new NullRequest.OnRequestListeren() {
            @Override
            public void onSuccess() {
                if (getView() == null) return;
                getView().onCollectSuccess(collect);
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onCollectFailure(message);
            }
        });
    }

    public void addCart(int goodId) {
        AddApi api = new AddApi();
        api.goodsId = goodId;
        api.request(new ObjectRequest.OnRequestListeren<MsgEntity>() {
            @Override
            public void onSuccess(MsgEntity value) {
                if (getView() == null) return;
                EventBus.getDefault().post(new CartEvent());
                getView().onAddCartSuccess("添加成功");
            }

            @Override
            public void onFailure(String message) {
                if (getView() == null) return;
                getView().onAddCaetFailure(message);
            }
        });
    }

    public void callPhone(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否拨打客服电话"); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PhoneUtils.call(context, Constant.PHONE_NUM);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
