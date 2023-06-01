package com.example.SearchMicroservice.Service;

import com.example.SearchMicroservice.DB.Repository.MovieRepository;
import com.example.SearchMicroservice.Model.MovieES;
import com.example.SearchMicroservice.Model.SubtitlesES;
import com.example.SearchMicroservice.Repository.MovieESRepository;
import com.example.SearchMicroservice.Repository.SubtitlesESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class SearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private MovieESRepository movieESRepository;

    @Autowired
    private SubtitlesESRepository SubtitlesRepository;

    @Autowired
    private MovieRepository movieRepository;


    // Search by movie parameters - title and author
    public List<MovieES> searchByMovie(String search){

        // Create criteria for search - string must appear in
        // title or in author's name
        Criteria criteria = new Criteria().or("title").contains(search)
                .or("author").contains(search);

        // Create a query from the criteria
        Query searchQuery = new CriteriaQuery(criteria);

        // Use query to find the movies
        SearchHits<MovieES> foundMovies =
                elasticsearchOperations
                        .search(searchQuery, MovieES.class,
                                IndexCoordinates.of("movieindex"));

        // Convert back to List:
        List<MovieES> movieList = new ArrayList<MovieES>();
        foundMovies.forEach(mov->{
            movieList.add(mov.getContent());
        });

        return movieList;
    }

    // Search by movie text
    public List<MovieES> searchByText(String search){

        List<SubtitlesES> textsFound = SubtitlesRepository.findBytextContaining(search);

        // Eliminate duplicates:
        HashSet<Object> seen=new HashSet<>();
        textsFound.removeIf(t -> !seen.add(t.getId()));

        // Get list of Movie(s) by movie_id field of Subtitles
        List<MovieES> foundMovies = new LinkedList<>();
        for (SubtitlesES txt : textsFound){
            foundMovies.add( movieESRepository.findById(txt.getMovie_id())
                            .orElse(null));
        }

        return foundMovies;
    }

    // Search by everything
    public List<MovieES> searchMov(String search){
        // Search by movie attributes and movie text
        List<MovieES> movSearch = searchByMovie(search);
        List<MovieES> txtSearch = searchByText(search);

        // Eliminate duplicates:
        txtSearch.removeAll(movSearch);

        // Concat the two lists:
        List<MovieES> found = Stream.concat(movSearch.stream(), txtSearch.stream()).toList();

        return found;
    }

    // Search by everything - return IDs instead of movies
    public List<Integer> search(String search){
        List<MovieES> found = searchMov(search);
        List<Integer> foundId = new LinkedList<>();
        for (MovieES mov: found){
            foundId.add(Integer.parseInt(mov.getId()));
        }
        return foundId;
    }

}
