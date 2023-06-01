package com.example.SearchMicroservice.Repository;

import com.example.SearchMicroservice.Model.SubtitlesES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
Movie model for Elastic Search.
 */

@Repository
public interface SubtitlesESRepository extends ElasticsearchRepository<SubtitlesES, String> {

    List<SubtitlesES> findBytextContaining(String s);
}
