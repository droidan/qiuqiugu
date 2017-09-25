package com.tiger.socol.gu.activity.product.assess;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.activity.mine.login.LoginEvent;
import com.tiger.socol.gu.activity.mine.order.OrderEvent;
import com.tiger.socol.gu.api.index.GoodsEntity;
import com.tiger.socol.gu.base.BaseViewStateActivity;
import com.tiger.socol.gu.constant.IntentConstant;
import com.tiger.socol.gu.network.GsonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AssessActivity extends BaseViewStateActivity<AssessView, AssessPresenter>
        implements AssessView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.cb_select)
    CheckBox cbSelect;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.mb_start)
    MaterialRatingBar mbStart;

    private int orderId;
    private AssessAdapter adapter;
    private LinearLayoutManager glm;

    @NonNull
    @Override
    public AssessPresenter createPresenter() {
        return new AssessPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assess;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void afterViewInit() {
        toolbar.setTitle("评价");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new AssessAdapter(this);
        glm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(glm);
        recyclerView.addItemDecoration(new SpacesItemDecoration());
        recyclerView.setAdapter(adapter);

        orderId = getIntent().getIntExtra(IntentConstant.ORDER_ID, 0);
        ArrayList<GoodsEntity> goods = getIntent().getParcelableArrayListExtra(IntentConstant.GOODS);
        adapter.setGoods(goods);
    }

    private String getComment() {
        ArrayList<GoodCommentEntity> commests = new ArrayList<>();
        for (int position = 0; position < adapter.getItemCount(); position++) {
            View view = recyclerView.getChildAt(position);
            if (null != recyclerView.getChildViewHolder(view)) {
                AssessAdapter.ViewHodler viewHolder = (AssessAdapter.ViewHodler) recyclerView.getChildViewHolder(view);
                int start = (int) viewHolder.mrbGood.getRating();
                int goodId = adapter.getGoods().get(position).getGoodsId();
                String content = viewHolder.edComment.getText().toString();

                GoodCommentEntity commentEntity = new GoodCommentEntity();
                commentEntity.setStart(start);
                commentEntity.setGoodId(goodId);
                commentEntity.setContent(content);
                commests.add(commentEntity);
            }
        }

        String json = GsonUtil.GsonString(commests);
        return json;
    }

    @Override
    public void onCommentSuccess() {
        showToask("评价成功");
        EventBus.getDefault().post(new OrderEvent(3));
        finish();
    }

    @Override
    public void onCommentFailure(String message) {
        showToask("评价失败");
    }

    @OnClick(R.id.bt_submit)
    public void onClick() {
        String comment = getComment();
        presenter.submit(orderId, mbStart.getRating(), comment, cbSelect.isChecked());
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = 20;
            } else {
                outRect.bottom = 0;
            }

            outRect.right = 0;
            outRect.top = 20;
            outRect.left = 0;
        }
    }

    public class GoodCommentEntity {
        private int start;
        private int goodId;
        private String content;

        public GoodCommentEntity() {
        }

        public GoodCommentEntity(int start, int goodId, String content) {
            this.start = start;
            this.goodId = goodId;
            this.content = content;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getGoodId() {
            return goodId;
        }

        public void setGoodId(int goodId) {
            this.goodId = goodId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}

