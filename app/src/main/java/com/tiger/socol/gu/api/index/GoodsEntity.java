package com.tiger.socol.gu.api.index;

import android.os.Parcel;
import android.os.Parcelable;

import com.tiger.socol.gu.utils.DoubleUtils;

public class GoodsEntity implements Parcelable {

    private int goodsId;
    private String goodsName;
    private String thumb;
    private Double price;
    private String created;
    private String status;
    private boolean isCollected;
    private boolean isAddToCar;
    private boolean isLive;
    private int num;
    private double expressWeight;
    private double width;
    private double height;
    private double stars;

    public double getTotlaPrice() {
        double p = (double) num * price;
        return DoubleUtils.toPrice(p);
    }



    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public boolean isAddToCar() {
        return isAddToCar;
    }

    public void setAddToCar(boolean addToCar) {
        isAddToCar = addToCar;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getExpressWeight() {
        return expressWeight;
    }

    public void setExpressWeight(double expressWeight) {
        this.expressWeight = expressWeight;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.goodsId);
        dest.writeString(this.goodsName);
        dest.writeString(this.thumb);
        dest.writeValue(this.price);
        dest.writeString(this.created);
        dest.writeString(this.status);
        dest.writeByte(this.isCollected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAddToCar ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLive ? (byte) 1 : (byte) 0);
        dest.writeInt(this.num);
        dest.writeDouble(this.expressWeight);
        dest.writeDouble(this.width);
        dest.writeDouble(this.height);
        dest.writeDouble(this.stars);
    }

    public GoodsEntity() {
    }

    protected GoodsEntity(Parcel in) {
        this.goodsId = in.readInt();
        this.goodsName = in.readString();
        this.thumb = in.readString();
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.created = in.readString();
        this.status = in.readString();
        this.isCollected = in.readByte() != 0;
        this.isAddToCar = in.readByte() != 0;
        this.isLive = in.readByte() != 0;
        this.num = in.readInt();
        this.expressWeight = in.readDouble();
        this.width = in.readDouble();
        this.height = in.readDouble();
        this.stars = in.readDouble();
    }

    public static final Creator<GoodsEntity> CREATOR = new Creator<GoodsEntity>() {
        @Override
        public GoodsEntity createFromParcel(Parcel source) {
            return new GoodsEntity(source);
        }

        @Override
        public GoodsEntity[] newArray(int size) {
            return new GoodsEntity[size];
        }
    };
}
