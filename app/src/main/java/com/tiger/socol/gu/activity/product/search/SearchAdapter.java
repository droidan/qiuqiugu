package com.tiger.socol.gu.activity.product.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiger.socol.gu.R;
import com.tiger.socol.gu.managers.KeywordMannager;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Viewhodler> {

    private List<String> historys = new ArrayList<>();
    private Context context;
    private OnSearchAdapterListener listener;

    public void setListener(OnSearchAdapterListener listener) {
        this.listener = listener;
    }

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public void setHistorys(List<String> historys) {
        this.historys = historys;
        notifyDataSetChanged();
    }

    @Override
    public Viewhodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history, parent, false);
        return new SearchAdapter.Viewhodler(view);
    }

    @Override
    public void onBindViewHolder(Viewhodler holder, final int position) {
        final String title = historys.get(position);
        holder.txTitle.setText(title);
        holder.imDetele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historys.remove(position);
                KeywordMannager.getInstance().delete(title);
                notifyDataSetChanged();
                if (historys.size() == 0) {
                    listener.onItemClean();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historys.size();
    }

    public class Viewhodler extends RecyclerView.ViewHolder {
        TextView txTitle;
        ImageView imDetele;

        public Viewhodler(View itemView) {
            super(itemView);
            txTitle = (TextView) itemView.findViewById(R.id.tx_title);
            imDetele = (ImageView) itemView.findViewById(R.id.bt_delete);
        }
    }

    public interface OnSearchAdapterListener {
        void onItemClick(String keyword);

        void onItemClean();
    }

}
