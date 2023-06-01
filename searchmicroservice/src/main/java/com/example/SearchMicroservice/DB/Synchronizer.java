package com.example.SearchMicroservice.DB;

import com.example.SearchMicroservice.DB.Model.Movie;
import com.example.SearchMicroservice.DB.Repository.MovieRepository;
import com.example.SearchMicroservice.DB.Model.Subtitles;
import com.example.SearchMicroservice.DB.Repository.SubtitlesDBRepository;
import com.example.SearchMicroservice.Model.MovieES;
import com.example.SearchMicroservice.Model.SubtitlesES;
import com.example.SearchMicroservice.Repository.MovieESRepository;
import com.example.SearchMicroservice.Repository.SubtitlesESRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/*
Synchronizer. Checks if tables in Postgres were modified and if yes, updates ES.
 */

@Service
public class Synchronizer {

    private MovieESRepository movieESRepo;                  // ES movie repo

    private MovieRepository movieDBRepo;                    // DB movie repo

    private SubtitlesESRepository SubtitleESsRepo;          // ES text repo
    private SubtitlesDBRepository SubtitlesDBRepo;          // DB text repo

    @Autowired
    private Mapper mapper;

    private Date lastSynchDate;
    private Date currentDate;

    @Autowired
    public Synchronizer(
            MovieESRepository movieESRepository,
            MovieRepository movieDBRepository,
            SubtitlesESRepository SubtitlesESRepository,
            SubtitlesDBRepository SubtitlesDBRepository){
        this.movieESRepo = movieESRepository;
        this.movieDBRepo = movieDBRepository;
        this.SubtitleESsRepo = SubtitlesESRepository;
        this.SubtitlesDBRepo = SubtitlesDBRepository;
        this.lastSynchDate = new Timestamp(System.currentTimeMillis());
    }

    // Scheduled to work once per minute
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void sync(){
        currentDate = new Timestamp(System.currentTimeMillis());

        syncMovies();
        syncText();

        lastSynchDate = new Timestamp(System.currentTimeMillis());

    }

    // Synchronizing Movie data
    public void syncMovies(){
        Specification<Movie> synchSpec = movieSpecification();

        List<Movie> movieDBList = movieDBRepo.findAll(synchSpec);

        List<MovieES> movieList = mapper.dbToESMovies(movieDBList);

        for(MovieES movie: movieList){
            movieESRepo.save(movie);
        }

    }

    // Synchronizing Subtitles data
    public void syncText(){
        Specification<Subtitles> synchSpec = textSpecification();

        List<Subtitles> textDBList = SubtitlesDBRepo.findAll(synchSpec);

        List<SubtitlesES> textList = mapper.dbToESTexts(textDBList);

        for(SubtitlesES text: textList){
            SubtitleESsRepo.save(text);
        }
    }

    // Specification for Movie update (update if modified between last sync and now)
    public Specification<Movie> movieSpecification(){
        return new Specification<Movie>() {
            @Override
            public jakarta.persistence.criteria.Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(
                        root.<Date>get("modificationDate"), lastSynchDate, currentDate
                );
            }
        };
    }

    // Specification for Subtitles update (update if modified between last sync and now)
    public Specification<Subtitles> textSpecification(){
        return new Specification<Subtitles>() {
            @Override
            public jakarta.persistence.criteria.Predicate toPredicate(Root<Subtitles> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.between(
                        root.<Date>get("modificationDate"), lastSynchDate, currentDate
                );
            }
        };
    }

}
