package com.tiger.socol.gu.activity.shopCart.paySelect;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tiger.socol.gu.R;


public class PayDialog extends Dialog implements View.OnClickListener {

    private CheckBox cbZaixian;
    private RelativeLayout rlZaixian;
    private CheckBox cbHuodao;
    private RelativeLayout rlHuodao;
    private ImageView close;
    private Button btSubmit;
    private int position = 0;

    private Context mContext;
    private OnPaySelectListeren listeren;

    public PayDialog(Context context) {
        super(context, R.style.Dialog_Fullscreen);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public PayDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void setListeren(OnPaySelectListeren listeren) {
        this.listeren = listeren;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = View.inflate(mContext, R.layout.activity_pay, null);
        initView(v);
        setContentView(v);
        setLayout();
    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
    }

    private void initView(View v) {
        close = (ImageView) v.findViewById(R.id.im_close);
        btSubmit = (Button) v.findViewById(R.id.bt_submit);
        close.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        cbHuodao = (CheckBox) v.findViewById(R.id.cb_huodao);
        rlHuodao = (RelativeLayout) v.findViewById(R.id.rl_huodao);
        rlHuodao.setOnClickListener(this);
        cbZaixian = (CheckBox) v.findViewById(R.id.cb_zaixian);
        rlZaixian = (RelativeLayout) v.findViewById(R.id.rl_zaixian);
        rlZaixian.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_zaixian:
                position = 0;
                cbHuodao.setChecked(false);
                cbZaixian.setChecked(true);
                break;

            case R.id.rl_huodao:
                position = 1;
                cbHuodao.setChecked(true);
                cbZaixian.setChecked(false);
                break;

            case R.id.im_close:
                dismiss();
                break;

            case R.id.bt_submit:
                listeren.onItemClick(position);
                dismiss();
                break;
        }
    }

    public interface OnPaySelectListeren {
        void onItemClick(int position);
    }

}
