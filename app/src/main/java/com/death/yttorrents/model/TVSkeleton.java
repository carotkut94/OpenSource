package com.death.yttorrents.model;

/**
 * Created by rajora_sd on 2/24/2017.
 */

public class TVSkeleton {

    private String Poster_Path;

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    private String first_air_date;
    private String overview;
    private String id;
    private String title;
    private String popularity;
    private String vote_average;


    public String getPoster_Path() {
        return Poster_Path;
    }

    public void setPoster_Path(String poster_Path) {
        Poster_Path = poster_Path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

}
