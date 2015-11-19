package com.example.andrea.musicreview.model;

import java.util.GregorianCalendar;

/**
 * Created by luca-campana on 19/11/15.
 */
public class MusicLike {
    private String name;
    private long id;
    private GregorianCalendar createdTime;

    public MusicLike(String name, long id, GregorianCalendar createdTime) {
        this.name = name;
        this.id = id;
        this.createdTime = createdTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GregorianCalendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(GregorianCalendar createdTime) {
        this.createdTime = createdTime;
    }
}
