package com.example.andrea.musicreview.model;

public class Artist {
    private int id;
    private String name;
    private String bio;

    public Artist(int id, String name, String bio) {
        this.id = id;
        this.name = name;
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }
}
