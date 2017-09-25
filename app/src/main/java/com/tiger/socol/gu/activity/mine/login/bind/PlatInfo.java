package com.tiger.socol.gu.activity.mine.login.bind;

import android.os.Parcel;
import android.os.Parcelable;

public class PlatInfo implements Parcelable {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 昵称
     */
    private String nikeName;
    /**
     * 平台名称
     */
    private String platName;
    /**
     * 头像
     */
    private String userAvatar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.nikeName);
        dest.writeString(this.platName);
        dest.writeString(this.userAvatar);
    }

    public PlatInfo() {
    }

    @Override
    public String toString() {
        return "PlatInfo{" +
                "userId='" + userId + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", platName='" + platName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }

    protected PlatInfo(Parcel in) {
        this.userId = in.readString();
        this.nikeName = in.readString();
        this.platName = in.readString();
        this.userAvatar = in.readString();
    }

    public static final Parcelable.Creator<PlatInfo> CREATOR = new Parcelable.Creator<PlatInfo>() {
        @Override
        public PlatInfo createFromParcel(Parcel source) {
            return new PlatInfo(source);
        }

        @Override
        public PlatInfo[] newArray(int size) {
            return new PlatInfo[size];
        }
    };

}
