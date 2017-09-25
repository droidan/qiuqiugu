package com.tiger.socol.gu.api.bbs;

import android.os.Parcel;
import android.os.Parcelable;

public class AdEntity implements Parcelable {
    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String thumb;
    private String value;
    private String type;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.value);
        dest.writeString(this.thumb);
    }

    public AdEntity() {
    }

    protected AdEntity(Parcel in) {
        this.type = in.readString();
        this.value = in.readString();
        this.thumb = in.readString();
    }

    public static final Parcelable.Creator<AdEntity> CREATOR = new Parcelable.Creator<AdEntity>() {
        @Override
        public AdEntity createFromParcel(Parcel source) {
            return new AdEntity(source);
        }

        @Override
        public AdEntity[] newArray(int size) {
            return new AdEntity[size];
        }
    };
}
