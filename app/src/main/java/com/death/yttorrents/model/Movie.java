package com.death.yttorrents.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 04/04/16.
 */
public class Movie implements Serializable{
    private String name;
    private String URL;

    private String rating;
    private String small, medium, large;
    private String timestamp;

    public String getSumary() {
        return sumary;
    }

    public void setSumary(String sumary) {
        this.sumary = sumary;
    }

    private String sumary;

    public Movie() {
    }

    public Movie(String name, String small, String medium, String large, String timestamp, String URL, String rating) {
        this.name = name;
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.timestamp = timestamp;
        this.URL = URL;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
