package com.example.SearchMicroservice.DB.Model;

import com.example.SearchMicroservice.DB.DTO.SubtitlesDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.util.Date;

/*
Model for Subtitles in Postgres DB.
 */

@Entity
@Data
@Table(name = "subtitles")
public class Subtitles {

    @Id
    private int id;

    private int movieId;

    private String text;
    
    private Float startTime;
    private Float duration;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modificationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovie_id() {
        return movieId;
    }

    public void setMovie_id(int movie_id) {
        this.movieId = movie_id;
    }

    public String gettext() {
        return text;
    }

    public void settext(String text) {
        this.text = text;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Float getStartTime() {
        return startTime;
    }

    public void setStartTime(Float startTime) {
        this.startTime = startTime;
    }

    public Float getEndTime() {
        return duration;
    }

    public void setEndTime(Float endTime) {
        this.duration = endTime;
    }


    public SubtitlesDTO toDTO(){
        SubtitlesDTO SubtitlesDTO = new SubtitlesDTO(
                getId(),
                getMovie_id(),
                gettext()
        );
        return SubtitlesDTO;
    }
}
