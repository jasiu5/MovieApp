package com.example.SearchMicroservice.DB.Repository;


import com.example.SearchMicroservice.DB.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/*
Repository for Movie in Postgres DB.
 */

@Repository
public interface MovieRepository extends
        JpaRepository<Movie, Integer>,
        JpaSpecificationExecutor<Movie> {
    public Movie getById(int id);
}
