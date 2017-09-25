package com.tiger.socol.gu.activity.mine.about;

import android.util.Log;

import com.baidu.bdocreader.BDocInfo;
import com.baidu.bdocreader.BDocView;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.base.BaseActivity;

import butterknife.BindView;

public class DocActivity extends BaseActivity {

    @BindView(R.id.dv_doc)
    BDocView dvDoc;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_doc;
    }

    @Override
    protected void initData() {
        String docId = "doc-hfhud3t4a5hawg1";
        String host = "BCEDOC";
        String token = "TOKEN";
        String docType = "xlsx";
        String thisDocDir = "";
        String docTitle = "4月各店必修课程完成情况";
        int startPage = 1;
        int totalPage = 2;
        BDocInfo docInfo = new BDocInfo(host, docId, docType, token)
                .setLocalFileDir(thisDocDir)
                .setTotalPage(totalPage)
                .setDocTitle(docTitle)
                .setStartPage(startPage);

        dvDoc.setOnDocLoadStateListener(new BDocView.OnDocLoadStateListener() {
            @Override
            public void onDocLoadComplete() {
                Log.d("test", "onDocLoadComplete");
            }
            @Override
            public void onDocLoadFailed(String errorDesc) {
                // errorDesc format: ERROR_XXXX_DESC(code=xxxxx)
                Log.d("test", "onDocLoadFailed errorDesc=" + errorDesc);
            }
            @Override
            public void onCurrentPageChanged(int currentPage) {
                // 记录当前页面
                Log.i("test", "currentPage = " + currentPage);
            }
        });

        dvDoc.setFontSize(30);

        // 加载文档
        dvDoc.loadDoc(docInfo);
    }

    @Override
    protected void afterViewInit() {

    }

}
