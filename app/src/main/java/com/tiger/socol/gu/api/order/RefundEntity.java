package com.tiger.socol.gu.api.order;

import com.google.gson.annotations.SerializedName;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.List;

public class RefundEntity {

    private int orderId;
    private String sn;
    private double totalPrice;
    private double goodsPrice;
    private int expressPrice;
    private String voucher;
    private int addressId;
    private String payMode;
    private String payWay;
    private String created;
    private String status;
    private String message;
    private RefundDataBean refundData;
    private List<GoodsEntity> goods;

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

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getExpressPrice() {
        return expressPrice;
    }

    public void setExpressPrice(int expressPrice) {
        this.expressPrice = expressPrice;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RefundDataBean getRefundData() {
        return refundData;
    }

    public void setRefundData(RefundDataBean refundData) {
        this.refundData = refundData;
    }

    public List<GoodsEntity> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsEntity> goods) {
        this.goods = goods;
    }

    public static class RefundDataBean {

        private int amount;
        @SerializedName("case")
        private String caseX;
        private String remark;
        private String created;
        private String handleTime;
        private String status;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCaseX() {
            return caseX;
        }

        public void setCaseX(String caseX) {
            this.caseX = caseX;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getHandleTime() {
            return handleTime;
        }

        public void setHandleTime(String handleTime) {
            this.handleTime = handleTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
