package com.tiger.socol.gu.activity.community.Content;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.tiger.socol.gu.R;


public class CommentDialog extends Dialog implements android.view.View.OnClickListener {

    private Button btCancle;
    private Button btSubmit;
    private Context mContext;
    private EditText edComment;
    private CommentDialogLislener lislener;

    public void setLislener(CommentDialogLislener lislener) {
        this.lislener = lislener;
    }

    public CommentDialog(Context context) {
        super(context, R.style.Dialog_Fullscreen);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public CommentDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = View.inflate(mContext, R.layout.dialog_comment_edit, null);
        initView(v);
        setContentView(v);
        setLayout();
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                edComment.setFocusableInTouchMode(true);
                edComment.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) edComment
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edComment,
                        InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private void initView(View v) {
        btCancle = (Button) v.findViewById(R.id.bt_cancle);
        btCancle.setOnClickListener(this);
        btSubmit = (Button) v.findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(this);
        edComment = (EditText) v.findViewById(R.id.ed_comment);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancle:
                dismiss();
                break;

            case R.id.bt_submit:
                dismiss();
                String content = edComment.getText().toString();
                if (StringUtils.isEmpty(content)) {
                    ToastUtils.showShortToast(mContext, "请输入评论");
                    return;
                }

                lislener.subimtComment(content);
                break;
        }
    }

    public interface CommentDialogLislener {
        void subimtComment(String content);
    }

}
