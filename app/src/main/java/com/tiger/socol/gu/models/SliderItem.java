package com.tiger.socol.gu.models;
import java.io.Serializable;

public class SliderItem implements Serializable {

    public String title;
    public String thumb;


    public SliderItem() {
    }

    public SliderItem(String thumb, String title) {
        this.thumb = thumb;
        this.title = title;
    }

}
