package com.example.SearchMicroservice.DB.Model;

import com.example.SearchMicroservice.DB.DTO.MovieDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/*
Model for Movie in Postgres DB.
 */

@Entity
@Data
@Table(name="movies")
public class Movie {

    @Id
    private int id;

    private String title;

    private String author;

    public int getId() {
        return id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modificationDate;

    private String description;
    private String url;

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

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public MovieDTO toDTO(){
        MovieDTO movieDTO = new MovieDTO(
                getId(),
                getTitle(),
                getAuthor()
        );
        return movieDTO;
    }

}
