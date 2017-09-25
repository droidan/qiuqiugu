package com.tiger.socol.gu.activity.mine.address.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.tiger.socol.gu.R;
import com.tiger.socol.gu.api.address.AddressEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AddressListAdapter extends BaseAdapter {

    private List<AddressEntity> list = new ArrayList<>();
    public void setList(List<AddressEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private LayoutInflater mInflater;
    private Context context;

    public List<AddressEntity> getList() {
        return list;
    }

    public AddressListAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_address, null);
            holder.txAddressInfo = (TextView) convertView.findViewById(R.id.tx_address_info);
            holder.txAddressName = (TextView) convertView.findViewById(R.id.tx_address_name);
            holder.txAddressPhone = (TextView) convertView.findViewById(R.id.tx_address_phone);
            holder.imDefult = (ImageView) convertView.findViewById(R.id.im_defult);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AddressEntity entity = list.get(position);
        holder.txAddressName.setText(entity.getName());
        holder.txAddressPhone.setText(entity.getPhone());
        holder.txAddressInfo.setText(entity.getProvince() + entity.getCity() + entity.getArea() + entity.getAddress());
        if (entity.isDefaultX()) {
            holder.imDefult.setVisibility(View.VISIBLE);
        } else {
            holder.imDefult.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView txAddressInfo;
        TextView txAddressName;
        TextView txAddressPhone;
        ImageView imDefult;
    }

}

