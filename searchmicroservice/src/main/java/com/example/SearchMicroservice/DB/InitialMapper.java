package com.example.SearchMicroservice.DB;

import com.example.SearchMicroservice.DB.Model.Movie;
import com.example.SearchMicroservice.DB.Model.Subtitles;
import com.example.SearchMicroservice.DB.Repository.MovieRepository;
import com.example.SearchMicroservice.DB.Repository.SubtitlesDBRepository;
import com.example.SearchMicroservice.Model.MovieES;
import com.example.SearchMicroservice.Model.SubtitlesES;
import com.example.SearchMicroservice.Repository.MovieESRepository;
import com.example.SearchMicroservice.Repository.SubtitlesESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
Class that reads all Postgres data (about Movie and Subtitles tables) and parses
into Elastic Search.
 */

@Service
public class InitialMapper {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieESRepository movieESRepository;

    @Autowired
    SubtitlesDBRepository subtitlesDBRepository;

    @Autowired
    SubtitlesESRepository subtitlesESRepository;


    @Autowired
    private Mapper mapper;

    public void init() {

        // Init ES database - movies
        List<Movie> movieList = movieRepository.findAll();
        List<MovieES> movieESList = mapper.dbToESMovies(movieList);
        for(MovieES movie: movieESList){
            movieESRepository.save(movie);
        }

        // Init ES database - subtitles
        List<Subtitles> subList = subtitlesDBRepository.findAll();
        List<SubtitlesES> subESList = mapper.dbToESTexts(subList);
        for(SubtitlesES sub: subESList){
            subtitlesESRepository.save(sub);
        }


    }

}
