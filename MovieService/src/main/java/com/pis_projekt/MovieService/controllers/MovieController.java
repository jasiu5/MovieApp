package com.pis_projekt.MovieService.controllers;

import com.pis_projekt.MovieService.services.MinIOService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MinIOService minioservice;

    @GetMapping()
    public List<String> getMovies() throws MinioException, IOException,
            NoSuchAlgorithmException, InvalidKeyException {
        return minioservice.getListOfMovies();
    }

    @GetMapping("/{movie_name}")
    public String getUrlMovie(@PathVariable(name = "movie_name") String movieName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return minioservice.getFileUrl(movieName);
    }

    @GetMapping("/download/{movie_name}")
    public File downloadMovie(@PathVariable(name = "movie_name") String movieName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return minioservice.ReadFromMinIO(movieName);
    }

    @PostMapping("/add_movie")
    public void uploadMovie(@RequestParam("file") MultipartFile multipartFile) throws NoSuchAlgorithmException, IOException, InvalidKeyException, MinioException {
        minioservice.WriteToMinIO(multipartFile);
    }
}
