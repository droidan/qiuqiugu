package com.tiger.socol.gu.api.menber;

public class ThreeLoginEntity {
    private boolean isBind;
    private Member data;

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    public Member getData() {
        return data;
    }

    public void setData(Member data) {
        this.data = data;
    }
}
