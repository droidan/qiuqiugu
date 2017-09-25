package com.tiger.socol.gu.api.farm;

import com.tiger.socol.gu.api.good.VideoEntity;
import com.tiger.socol.gu.api.index.GoodsEntity;

import java.util.List;

public class FarmDetailEntity {

    private int baseId;
    private String baseName;
    private String thumb;
    private VideoEntity live;
    private int pv;
    private String created;
    private String content;
    private GoodsBean goods;
    private GoodsBean related;
    private String liveUrl;

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public VideoEntity getLive() {
        return live;
    }

    public void setLive(VideoEntity live) {
        this.live = live;
    }

    public GoodsBean getRelated() {
        return related;
    }

    public void setRelated(GoodsBean related) {
        this.related = related;
    }

    public int getBaseId() {
        return baseId;
    }

    public void setBaseId(int baseId) {
        this.baseId = baseId;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public GoodsBean getGoods() {
        return goods;
    }

    public void setGoods(GoodsBean goods) {
        this.goods = goods;
    }

    public static class GoodsBean {

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
