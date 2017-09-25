package com.tiger.socol.gu.api.order;

public class OrderNumEntity {
    private int Unpaid;
    private int Pending;
    private int Dispatch;
    private int Complete;
    private int Cancel;
    private int Refund;
    private int Refunded;

    public int getUnpaid() {
        return Unpaid;
    }

    public void setUnpaid(int unpaid) {
        Unpaid = unpaid;
    }

    public int getPending() {
        return Pending;
    }

    public void setPending(int pending) {
        Pending = pending;
    }

    public int getDispatch() {
        return Dispatch;
    }

    public void setDispatch(int dispatch) {
        Dispatch = dispatch;
    }

    public int getComplete() {
        return Complete;
    }

    public void setComplete(int complete) {
        Complete = complete;
    }

    public int getCancel() {
        return Cancel;
    }

    public void setCancel(int cancel) {
        Cancel = cancel;
    }

    public int getRefund() {
        return Refund;
    }

    public void setRefund(int refund) {
        Refund = refund;
    }

    public int getRefunded() {
        return Refunded;
    }

    public void setRefunded(int refunded) {
        Refunded = refunded;
    }
}
