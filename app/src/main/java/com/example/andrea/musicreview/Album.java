package com.example.andrea.musicreview;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Album {

    private int id;
    private String title;
    private Artist artist;
    private String genre;
    private GregorianCalendar releaseDate = new GregorianCalendar();
    private GregorianCalendar dateAdded = new GregorianCalendar();
    private String imageURL;
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
            this.id = Integer.parseInt((String) source.get("IDAlbum"));
            this.title = (String) source.get("Title");
            this.artist = new Artist(Integer.parseInt((String) source.get("IDArtist")), (String) source.get("Name"), (String) source.get("Bio"));
            this.genre = (String) source.get("Genre");
            this.releaseDate.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY).parse((String) source.get("Release_date")));
            this.imageURL = (String) source.get("Image");
            if(!source.get("Spotify_URI").equals("")){
                this.spotifyURI = (String) source.get("Spotify_URI");
            }
            this.dateAdded.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ITALY).parse((String) source.get("Date_added")));
            this.grade = Integer.parseInt((String) source.get("Grade"));
            this.review = (String) source.get("Review");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Artist getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public GregorianCalendar getReleaseDate() {
        return releaseDate;
    }

    public String getImageURL() {
        return imageURL;
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
}
