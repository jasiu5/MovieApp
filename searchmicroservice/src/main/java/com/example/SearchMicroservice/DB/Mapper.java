package com.example.SearchMicroservice.DB;


import com.example.SearchMicroservice.DB.DTO.MovieDTO;
import com.example.SearchMicroservice.DB.DTO.SubtitlesDTO;
import com.example.SearchMicroservice.DB.Model.Movie;
import com.example.SearchMicroservice.DB.Model.Subtitles;
import com.example.SearchMicroservice.Model.MovieES;
import com.example.SearchMicroservice.Model.SubtitlesES;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/*
Mapper for Movie and Subtitles between ES model and Postgres model (and vice versa).
Used in Synchronizer class to update ES data when Postgres data is modified.
 */

@Service
public class Mapper {


    public MovieES dbToESMovie(Movie movieDB){
        MovieDTO dto = movieDB.toDTO();
        MovieES movie = new MovieES();
        movie.setId(Integer.toString(dto.getId()));
        movie.setTitle(dto.getTitle());
        movie.setAuthor(dto.getAuthor());
        return movie;
    }

    public List<MovieES> dbToESMovies(List<Movie> movieDBList){
        List<MovieES> movies = new LinkedList<>();
        for (Movie mov: movieDBList){
            movies.add(dbToESMovie(mov));
        }
        return movies;
    }

    public SubtitlesES dbToESText(Subtitles SubtitlesDB){
        SubtitlesDTO dto = SubtitlesDB.toDTO();
        SubtitlesES text = new SubtitlesES();
        text.setId(Integer.toString(dto.getId()));
        text.setMovie_id(Integer.toString(dto.getMovieId()));
        text.settext(dto.gettext());
        return text;
    }

    public List<SubtitlesES> dbToESTexts(List<Subtitles> textDBList){
        List<SubtitlesES> texts = new LinkedList<>();
        for (Subtitles txt: textDBList){
            texts.add(dbToESText(txt));
        }
        return texts;
    }

}
