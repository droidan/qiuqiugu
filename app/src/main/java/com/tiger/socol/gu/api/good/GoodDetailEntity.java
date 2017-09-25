package com.tiger.socol.gu.api.good;

import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.List;

public class GoodDetailEntity extends GoodsEntity {

    private String description;
    private String categoryName;
    private String baseName;
    private int baseId;
    private String content;
    private int pv;
    private int likes;
    private String liveUrl;

    private CommentsBean comments;
    private int stock;
    private String unit;
    private VideoEntity live;
    private RelatedBean related;

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public int getBaseId() {
        return baseId;
    }

    public void setBaseId(int baseId) {
        this.baseId = baseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public CommentsBean getComments() {
        return comments;
    }

    public void setComments(CommentsBean comments) {
        this.comments = comments;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public VideoEntity getLive() {
        return live;
    }

    public void setLive(VideoEntity live) {
        this.live = live;
    }

    public RelatedBean getRelated() {
        return related;
    }

    public void setRelated(RelatedBean related) {
        this.related = related;
    }

    public static class CommentsBean {

        private int total;

        private List<GoodsCommentEntity> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<GoodsCommentEntity> getData() {
            return data;
        }

        public void setData(List<GoodsCommentEntity> data) {
            this.data = data;
        }

    }

    public static class RelatedBean {

        private int total;

        private List<GoodsEntity> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<GoodsEntity> getData() {
            return data;
        }

        public void setData(List<GoodsEntity> data) {
            this.data = data;
        }

    }
}
