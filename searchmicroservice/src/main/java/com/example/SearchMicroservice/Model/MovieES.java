package com.example.SearchMicroservice.Model;

import com.example.SearchMicroservice.DB.DTO.MovieDTO;
import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/*
Movie model for Elastic Search.
 */

@Document(indexName = "movieindex")
public class MovieES {

    @Id
    private String id;

    @Field(type= FieldType.Text, name = "title")
    private String title;

    @Field(type=FieldType.Text, name = "author")
    private String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public MovieDTO toDTO(){
        MovieDTO movieDTO = new MovieDTO(
                Integer.parseInt(getId()),
                getTitle(),
                getAuthor()
        );
        return  movieDTO;
    }

    @Override
    public boolean equals(Object object){
        if (!(object instanceof MovieES)){
            return false;
        }
        MovieES mov = (MovieES) object;
        return mov.getId().equals(getId());
    }
}
