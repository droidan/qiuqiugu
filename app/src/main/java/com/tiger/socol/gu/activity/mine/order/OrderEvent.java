package com.tiger.socol.gu.activity.mine.order;

public class OrderEvent {

    private int type = 0;

    public OrderEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
