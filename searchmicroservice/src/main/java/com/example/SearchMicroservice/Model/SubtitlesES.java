package com.example.SearchMicroservice.Model;


import com.example.SearchMicroservice.DB.DTO.SubtitlesDTO;
import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/*
Subtitles model for Elastic Search.
 */

@Document(indexName = "subtitlesindex")
public class SubtitlesES {

    @Id
    private String id;

    @Field(type = FieldType.Text, name= "movieid")
    private String movie_id;

    @Field(type = FieldType.Text, name="text")
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String gettext() {
        return text;
    }

    public void settext(String text) {
        this.text = text;
    }

    public SubtitlesDTO toDTO(){
        SubtitlesDTO SubtitlesDTO = new SubtitlesDTO(
                Integer.parseInt(getId()),
                Integer.parseInt(getMovie_id()),
                gettext()
        );
        return SubtitlesDTO;
    }
}
