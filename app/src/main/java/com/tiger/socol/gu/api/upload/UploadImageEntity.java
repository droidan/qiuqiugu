package com.tiger.socol.gu.api.upload;

import java.util.List;

public class UploadImageEntity {

    private int total;
    private int successNum;
    private List<ImageInfoEntity> successData;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public List<ImageInfoEntity> getSuccessData() {
        return successData;
    }

    public void setSuccessData(List<ImageInfoEntity> successData) {
        this.successData = successData;
    }
}
