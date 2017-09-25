package com.tiger.socol.gu.api.bbs;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BBSEntity implements Parcelable {

    private int userId;
    private int bbsId;
    private String content;
    private int likes;
    private int comments;
    private String created;
    private AttachmentBean attachment;
    private String avatar;
    private String nickName;
    private boolean isLike;


    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBbsId() {
        return bbsId;
    }

    public void setBbsId(int bbsId) {
        this.bbsId = bbsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public AttachmentBean getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentBean attachment) {
        this.attachment = attachment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public static class AttachmentBean implements Parcelable {
        private List<String> thumb;
        private List<String> org;

        public List<String> getThumb() {
            return thumb;
        }

        public void setThumb(List<String> thumb) {
            this.thumb = thumb;
        }

        public List<String> getOrg() {
            return org;
        }

        public void setOrg(List<String> org) {
            this.org = org;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringList(this.thumb);
            dest.writeStringList(this.org);
        }

        public AttachmentBean() {
        }

        protected AttachmentBean(Parcel in) {
            this.thumb = in.createStringArrayList();
            this.org = in.createStringArrayList();
        }

        public static final Creator<AttachmentBean> CREATOR = new Creator<AttachmentBean>() {
            @Override
            public AttachmentBean createFromParcel(Parcel source) {
                return new AttachmentBean(source);
            }

            @Override
            public AttachmentBean[] newArray(int size) {
                return new AttachmentBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.bbsId);
        dest.writeString(this.content);
        dest.writeInt(this.likes);
        dest.writeInt(this.comments);
        dest.writeString(this.created);
        dest.writeParcelable(this.attachment, flags);
        dest.writeString(this.avatar);
        dest.writeString(this.nickName);
        dest.writeByte(this.isLike ? (byte) 1 : (byte) 0);
    }

    public BBSEntity() {
    }

    protected BBSEntity(Parcel in) {
        this.userId = in.readInt();
        this.bbsId = in.readInt();
        this.content = in.readString();
        this.likes = in.readInt();
        this.comments = in.readInt();
        this.created = in.readString();
        this.attachment = in.readParcelable(AttachmentBean.class.getClassLoader());
        this.avatar = in.readString();
        this.nickName = in.readString();
        this.isLike = in.readByte() != 0;
    }

    public static final Creator<BBSEntity> CREATOR = new Creator<BBSEntity>() {
        @Override
        public BBSEntity createFromParcel(Parcel source) {
            return new BBSEntity(source);
        }

        @Override
        public BBSEntity[] newArray(int size) {
            return new BBSEntity[size];
        }
    };
}
