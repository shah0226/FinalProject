package com.example.finalproject;

public class SavedFavourite {
    private long id;
    private String artist;
    private String title;
    private String lyrics;

    SavedFavourite(String artist, String title, String lyrics, long id) {
        this.artist = artist;
        this.title = title;
        this.lyrics = lyrics;
        this.id = id;
    }

    public long getId() { return id; }

    public String getArtist() { return artist; }

    public String getTitle() { return title; }

    public String getLyrics() { return lyrics; }

}
