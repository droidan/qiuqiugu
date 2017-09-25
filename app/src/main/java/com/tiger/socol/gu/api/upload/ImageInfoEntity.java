package com.tiger.socol.gu.api.upload;

public class ImageInfoEntity {

    private String key;

    private ViewBean view;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ViewBean getView() {
        return view;
    }

    public void setView(ViewBean view) {
        this.view = view;
    }

    public static class ViewBean {

        private String thumbPath;

        private String fullPath;

        public String getThumbPath() {
            return thumbPath;
        }

        public void setThumbPath(String thumbPath) {
            this.thumbPath = thumbPath;
        }

        public String getFullPath() {
            return fullPath;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }
    }
}
