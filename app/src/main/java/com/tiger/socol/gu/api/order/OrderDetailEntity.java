package com.tiger.socol.gu.api.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.List;

public class OrderDetailEntity implements Parcelable {
    private int orderId;
    private String sn;
    private double totalPrice;
    private int addressId;
    private String created;
    private DistributionBean distribution;
    private AddressInfoBean addressInfo;
    private String status;
    private int commend;
    private List<GoodsEntity> goods;
    private String payMode;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public DistributionBean getDistribution() {
        return distribution;
    }

    public void setDistribution(DistributionBean distribution) {
        this.distribution = distribution;
    }

    public AddressInfoBean getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfoBean addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCommend() {
        return commend;
    }

    public void setCommend(int commend) {
        this.commend = commend;
    }

    public List<GoodsEntity> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsEntity> goods) {
        this.goods = goods;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public static class DistributionBean implements Parcelable {


        private String expressNo;
        private String expressName;
        private String expressTime;

        public String getExpressNo() {
            return expressNo;
        }

        public void setExpressNo(String expressNo) {
            this.expressNo = expressNo;
        }

        public String getExpressName() {
            return expressName;
        }

        public void setExpressName(String expressName) {
            this.expressName = expressName;
        }

        public String getExpressTime() {
            return expressTime;
        }

        public void setExpressTime(String expressTime) {
            this.expressTime = expressTime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.expressNo);
            dest.writeString(this.expressName);
            dest.writeString(this.expressTime);
        }

        public DistributionBean() {
        }

        protected DistributionBean(Parcel in) {
            this.expressNo = in.readString();
            this.expressName = in.readString();
            this.expressTime = in.readString();
        }

        public static final Parcelable.Creator<DistributionBean> CREATOR = new Parcelable.Creator<DistributionBean>() {
            @Override
            public DistributionBean createFromParcel(Parcel source) {
                return new DistributionBean(source);
            }

            @Override
            public DistributionBean[] newArray(int size) {
                return new DistributionBean[size];
            }
        };
    }

    public static class AddressInfoBean implements Parcelable {
        private String name;
        private String phone;
        private String address;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.phone);
            dest.writeString(this.address);
        }

        public AddressInfoBean() {
        }

        protected AddressInfoBean(Parcel in) {
            this.name = in.readString();
            this.phone = in.readString();
            this.address = in.readString();
        }

        public static final Parcelable.Creator<AddressInfoBean> CREATOR = new Parcelable.Creator<AddressInfoBean>() {
            @Override
            public AddressInfoBean createFromParcel(Parcel source) {
                return new AddressInfoBean(source);
            }

            @Override
            public AddressInfoBean[] newArray(int size) {
                return new AddressInfoBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.orderId);
        dest.writeString(this.sn);
        dest.writeDouble(this.totalPrice);
        dest.writeInt(this.addressId);
        dest.writeString(this.created);
        dest.writeParcelable(this.distribution, flags);
        dest.writeParcelable(this.addressInfo, flags);
        dest.writeString(this.status);
        dest.writeInt(this.commend);
        dest.writeTypedList(this.goods);
        dest.writeString(this.payMode);
    }

    public OrderDetailEntity() {
    }

    protected OrderDetailEntity(Parcel in) {
        this.orderId = in.readInt();
        this.sn = in.readString();
        this.totalPrice = in.readDouble();
        this.addressId = in.readInt();
        this.created = in.readString();
        this.distribution = in.readParcelable(DistributionBean.class.getClassLoader());
        this.addressInfo = in.readParcelable(AddressInfoBean.class.getClassLoader());
        this.status = in.readString();
        this.commend = in.readInt();
        this.goods = in.createTypedArrayList(GoodsEntity.CREATOR);
        this.payMode = in.readString();
    }

    public static final Creator<OrderDetailEntity> CREATOR = new Creator<OrderDetailEntity>() {
        @Override
        public OrderDetailEntity createFromParcel(Parcel source) {
            return new OrderDetailEntity(source);
        }

        @Override
        public OrderDetailEntity[] newArray(int size) {
            return new OrderDetailEntity[size];
        }
    };
}
