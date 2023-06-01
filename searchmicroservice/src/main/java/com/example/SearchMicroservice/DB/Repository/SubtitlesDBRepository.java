package com.example.SearchMicroservice.DB.Repository;

import com.example.SearchMicroservice.DB.Model.Subtitles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/*
Repository for Subtitles in Postgres DB.
 */

@Repository
public interface SubtitlesDBRepository extends
        JpaRepository<Subtitles, Integer>,
        JpaSpecificationExecutor<Subtitles> {
}
