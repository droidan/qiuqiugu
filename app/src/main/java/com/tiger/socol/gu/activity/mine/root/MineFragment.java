package com.tiger.socol.gu.activity.mine.root;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.utils.PhoneUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.about.AboutActivity;
import com.tiger.socol.gu.activity.mine.about.DocActivity;
import com.tiger.socol.gu.activity.mine.collection.CollectionActivity;
import com.tiger.socol.gu.activity.mine.editInfo.EditInfoActivity;
import com.tiger.socol.gu.activity.mine.editInfo.password.EditPasswordActivity;
import com.tiger.socol.gu.activity.mine.login.LoginActivity;
import com.tiger.socol.gu.activity.mine.login.bind.BindActivity;
import com.tiger.socol.gu.activity.mine.order.OrderActivity;
import com.tiger.socol.gu.api.menber.Member;
import com.tiger.socol.gu.api.order.OrderNumEntity;
import com.tiger.socol.gu.base.BaseViewStateFragment;
import com.tiger.socol.gu.constant.Constant;
import com.tiger.socol.gu.managers.MemberMannager;
import com.tiger.socol.gu.views.widgets.BadgeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends BaseViewStateFragment<MineView, MinePresenter>
        implements MineView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tx_name)
    TextView txName;
    @BindView(R.id.im_avatar)
    RoundedImageView imAvatar;
    @BindView(R.id.bt_logout)
    Button btLogout;

    BadgeView zhiBadge;
    BadgeView faBadge;
    BadgeView shouBadge;
    BadgeView pinBadge;
    BadgeView tuiBadge;

    @BindView(R.id.bt_dai)
    Button btDai;
    @BindView(R.id.bt_fa)
    Button btFa;
    @BindView(R.id.bt_shou)
    Button btShou;
    @BindView(R.id.bt_ping)
    Button btPing;
    @BindView(R.id.bt_tui)
    Button btTui;

    @NonNull
    @Override
    public MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.orderNum();
        if (MemberMannager.getInstance().isLogin()) {
            Member member = MemberMannager.getInstance().getLoginUser();
            txName.setText(member.getNickName());
            if (!StringUtils.isEmpty(member.getAvatar())) {
                Picasso.with(getActivity()).load(member.getAvatar()).into(imAvatar);
            }
            btLogout.setVisibility(View.VISIBLE);
        } else {
            txName.setText("登录 / 注册");
            btLogout.setVisibility(View.GONE);
            imAvatar.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.my_head_portrait));
        }
    }

    @Override
    protected void aftertView() {
        toolbar.setTitle("我的");

        int badgeTextSize = 11;
        zhiBadge = new BadgeView(getContext(), btDai);
        zhiBadge.setTextSize(badgeTextSize);

        faBadge = new BadgeView(getContext(), btFa);
        faBadge.setTextSize(badgeTextSize);

        shouBadge = new BadgeView(getContext(), btShou);
        shouBadge.setTextSize(badgeTextSize);

        pinBadge = new BadgeView(getContext(), btPing);
        pinBadge.setTextSize(badgeTextSize);

        tuiBadge = new BadgeView(getContext(), btTui);
        tuiBadge.setTextSize(badgeTextSize);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine;
    }

    @Override
    protected void initView(View view) {

    }

    @OnClick({R.id.rl_tsf_edit, R.id.msiv_tsf_collect, R.id.msiv_tsf_call, R.id.bt_dai, R.id.bt_fa,
            R.id.bt_ping, R.id.bt_tui, R.id.bt_shou, R.id.bt_logout, R.id.msiv_tsf_password,
            R.id.msiv_tsf_about, R.id.msiv_tsf_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_tsf_edit:
                if (MemberMannager.getInstance().isLogin()) {
                    // 编辑个人资料
                    startActi(EditInfoActivity.class);
                } else {
                    startActi(LoginActivity.class);
                }

                break;

            case R.id.msiv_tsf_collect:
                // 我的收藏
                if (!MemberMannager.checkLogin(getActivity())) {
                    return;
                }
                startActi(CollectionActivity.class);
                break;

            case R.id.msiv_tsf_call:
                // 联系我们
                callPhone();
                break;

            case R.id.bt_dai:
                // 待支付
                if (!MemberMannager.checkLogin(getActivity())) {
                    return;
                }
                skipOrderActivity(0);
                break;

            case R.id.bt_fa:
                // 待发货
                if (!MemberMannager.checkLogin(getActivity())) {
                    return;
                }
                skipOrderActivity(1);
                break;

            case R.id.bt_shou:
                // 待收货
                if (!MemberMannager.checkLogin(getActivity())) {
                    return;
                }
                skipOrderActivity(2);
                break;

            case R.id.bt_ping:
                // 待评论
                if (!MemberMannager.checkLogin(getActivity())) {
                    return;
                }
                skipOrderActivity(3);
                break;

            case R.id.bt_tui:
                // 退货
                if (!MemberMannager.checkLogin(getActivity())) {
                    return;
                }
                skipOrderActivity(4);
                break;

            case R.id.msiv_tsf_password:
                // 修改密码
                if (!MemberMannager.checkLogin(getActivity())) {
                    return;
                }
                startActi(EditPasswordActivity.class);
                break;

            case R.id.bt_logout:
                presenter.logout();
                break;

            case R.id.msiv_tsf_about:
                startActi(DocActivity.class);
                break;

            case R.id.msiv_tsf_phone:
                Intent intent = new Intent();
                intent.setClass(getActivity(), BindActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void callPhone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否拨打客服电话"); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PhoneUtils.call(getActivity(), Constant.PHONE_NUM);
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

    private void skipOrderActivity(int typeIndex) {
        Intent intent = new Intent();
        intent.putExtra(OrderActivity.ORDER_TYPE_KEY, typeIndex);
        intent.setClass(getActivity(), OrderActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLogoutSuccess() {
        onResume();
    }

    @Override
    public void onLogoutFailure(String message) {
        showToask("退出失败");
    }

    @Override
    public void onReloadNumSuccess(OrderNumEntity entity) {
        setbadege(entity.getComplete(), pinBadge);
        setbadege(entity.getRefund(), tuiBadge);
        setbadege(entity.getDispatch(), shouBadge);
        setbadege(entity.getPending(), faBadge);
        setbadege(entity.getUnpaid(), zhiBadge);
    }

    private void setbadege(int num, BadgeView badge) {
        if (num == 0) {
            badge.hide();
        } else {
            badge.setText(num + "");
            badge.show();
        }
    }

    @Override
    public void onReloadNumFailure(String message) {
//        showToask(message);
    }

}