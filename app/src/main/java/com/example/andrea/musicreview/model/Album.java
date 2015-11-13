package com.example.andrea.musicreview.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Album {

    private AlbumBasicInfo basicInfo;
    private String genre;
    private GregorianCalendar releaseDate = new GregorianCalendar();
    private GregorianCalendar dateAdded = new GregorianCalendar();
    private String spotifyURI;
    private int grade;
    private String review;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    private boolean isFavorite;

    public Album(JSONObject source) throws ParseException {
        try {
            this.basicInfo = new AlbumBasicInfo(Integer.parseInt((String) source.get("IDAlbum")),
                    (String) source.get("Title"),
                    new Artist(Integer.parseInt((String) source.get("IDArtist")), (String) source.get("Name"), null),
                    (String) source.get("Image"));
            this.genre = (String) source.get("Genre");
            this.releaseDate.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY).parse((String) source.get("Release_date")));
            this.spotifyURI = (String) source.get("Spotify_URI");
            this.dateAdded.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ITALY).parse((String) source.get("Date_added")));
            this.grade = Integer.parseInt((String) source.get("Grade"));
            this.review = (String) source.get("Review");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return basicInfo.getId();
    }

    public String getTitle() {
        return basicInfo.getTitle();
    }

    public Artist getArtist() {
        return basicInfo.getArtist();
    }

    public String getGenre() {
        return genre;
    }

    public GregorianCalendar getReleaseDate() {
        return releaseDate;
    }

    public String getImageURL() {
        return basicInfo.getImageURL();
    }

    public String getSpotifyURI() {
        return spotifyURI;
    }

    public GregorianCalendar getDateAdded() {
        return dateAdded;
    }

    public int getGrade() {
        return grade;
    }

    public String getReview() {
        return review;
    }

    public static class AlbumBasicInfo{

        private int id;
        private String title;
        private Artist artist;
        private String imageURL;

        public AlbumBasicInfo(JSONObject source) throws ParseException {
            try {
                this.id = Integer.parseInt((String) source.get("IDAlbum"));
                this.title = (String) source.get("Title");
                this.artist = new Artist(Integer.parseInt((String) source.get("IDArtist")), (String) source.get("Name"), null);
                this.imageURL = (String) source.get("Image");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public AlbumBasicInfo(int id, String title, Artist artist, String imgURL) {
            this.id = id;
            this.title = title;
            this.artist = artist;
            this.imageURL = imgURL;
        }

        public int getId() {
            return id;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getTitle() {
            return title;
        }

        public Artist getArtist() {
            return artist;
        }
    }
}
