package com.example.andrea.musicreview;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Album {
    int id;
    String title;
    Artist artist;
    String genre;
    GregorianCalendar releaseDate = new GregorianCalendar();
    String imageURL;
    String spotifyURI;
    GregorianCalendar dateAdded = new GregorianCalendar();
    int grade;
    String review;

    public Album(JSONObject source) throws ParseException {
        try {
            this.id = Integer.parseInt((String) source.get("IDAlbum"));
            this.title = (String) source.get("Title");
            this.artist = new Artist(Integer.parseInt((String) source.get("IDArtist")), (String) source.get("Name"), (String) source.get("Bio"));
            this.genre = (String) source.get("Genre");
            this.releaseDate.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY).parse((String) source.get("Release_date")));
            this.imageURL = (String) source.get("Image");
            this.spotifyURI = (String) source.get("Spotify_URI");
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
