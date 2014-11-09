package com.athley.spinmywardrobe.model;

import com.athley.spinmywardrobe.Category;

import java.util.Date;

/**
 * Created by sathley on 11/6/2014.
 */
public class Item {

    private long itemId;

    private Category category;

    private String name;

    private String imageId;

    private String imageUrl;

    private Date lastWorn;

    private int timesWorn;

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getLastWorn() {
        return lastWorn;
    }

    public void setLastWorn(Date lastWorn) {
        this.lastWorn = lastWorn;
    }

    public int getTimesWorn() {
        return timesWorn;
    }

    public void setTimesWorn(int timesWorn) {
        this.timesWorn = timesWorn;
    }
}
