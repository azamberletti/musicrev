package com.example.andrea.musicreview.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Artist {
    private ArtistBasicInfo basicInfo;
    private String bio;

    public Artist(JSONObject source) {
        try {
            this.basicInfo = new ArtistBasicInfo(
                    Integer.parseInt((String) source.get("IDArtist")),
                    (String)source.get("Name")
            );
            this.bio = (String)source.get("Bio");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Artist(int id, String name, String bio) {
        this.basicInfo = new ArtistBasicInfo(id, name);
        this.bio = bio;
    }

    public int getId(){
        return this.basicInfo.getId();
    }

    public String getName(){
        return this.basicInfo.getName();
    }

    public String getBio() {
        return bio;
    }

    public static class ArtistBasicInfo{

        private int id;
        private String name;

        public ArtistBasicInfo(JSONObject source) {
            try {
                this.id = Integer.parseInt((String) source.get("IDArtist"));
                this.name = (String)source.get("Name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public ArtistBasicInfo(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
