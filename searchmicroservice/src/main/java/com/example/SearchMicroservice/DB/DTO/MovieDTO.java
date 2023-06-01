package com.example.SearchMicroservice.DB.DTO;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

public class MovieDTO {

    private int id;
    private String title;
    private String author;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date date;

    public MovieDTO(){}

    public MovieDTO(int id, String title, String author){
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
