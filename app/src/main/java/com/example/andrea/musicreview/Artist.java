package com.example.andrea.musicreview;

public class Artist {
    int id;
    String name;
    String bio;

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
