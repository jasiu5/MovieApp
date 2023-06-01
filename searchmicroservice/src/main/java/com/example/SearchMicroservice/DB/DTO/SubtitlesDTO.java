package com.example.SearchMicroservice.DB.DTO;

public class SubtitlesDTO {

    private int id;
    private int movieId;
    private String text;

    public SubtitlesDTO(){}

    public SubtitlesDTO(int id, int movieId, String text){
        this.id = id;
        this.movieId = movieId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String gettext() {
        return text;
    }

    public void settext(String text) {
        this.text = text;
    }
}
