package com.tiger.socol.gu.api.menber;

import io.realm.RealmObject;

public class Member extends RealmObject {

    private int userId;
    private String nickName;
    private String trueName;
    private String phone;
    private String avatar;
    private String lastIp;
    private String lastTime;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Member(Member member) {
        setUserId(member.getUserId());
        setNickName(member.getNickName());
        setTrueName(member.getTrueName());
        setPhone(member.getPhone());
        setAvatar(member.getAvatar());
        setLastIp(member.getLastIp());
        setLastTime(member.getLastTime());
        setStatus(member.getStatus());
    }

    public Member() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

}
