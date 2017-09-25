package com.tiger.socol.gu.api.good;

public class VideoEntity {

    private boolean status;
    private AddressBean address;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public class AddressBean {

        private String hd;
        private String sd;

        public String getHd() {
            return hd;
        }

        public void setHd(String hd) {
            this.hd = hd;
        }

        public String getSd() {
            return sd;
        }

        public void setSd(String sd) {
            this.sd = sd;
        }
    }
}
