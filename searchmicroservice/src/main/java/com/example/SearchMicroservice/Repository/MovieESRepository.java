package com.example.SearchMicroservice.Repository;

import com.example.SearchMicroservice.Model.MovieES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
Movie Repository for Elastic Search.
 */

@Repository
public interface MovieESRepository extends ElasticsearchRepository<MovieES, String> {

    List<MovieES> findAll();
    Optional<MovieES> findById(String id);

}
