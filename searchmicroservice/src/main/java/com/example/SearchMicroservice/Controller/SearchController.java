package com.example.SearchMicroservice.Controller;

import com.example.SearchMicroservice.Model.MovieES;
import com.example.SearchMicroservice.Service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    SearchService searchService;

    // Controller returning list of ids of movies
    @PostMapping("/search")
    public List<Integer> search(@RequestBody String s){
        List<Integer> found = searchService.search(s);
        return found;
    }

}
