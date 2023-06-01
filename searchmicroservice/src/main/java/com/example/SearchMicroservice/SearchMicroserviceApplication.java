package com.example.SearchMicroservice;

import com.example.SearchMicroservice.DB.InitialMapper;
import com.example.SearchMicroservice.DB.Model.Movie;
import com.example.SearchMicroservice.DB.Repository.MovieRepository;
import com.example.SearchMicroservice.Model.MovieES;
import com.example.SearchMicroservice.Repository.MovieESRepository;
import com.example.SearchMicroservice.Repository.SubtitlesESRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@EnableElasticsearchRepositories(basePackages = "com.example.SearchMicroservice.Repository")
@EnableJpaRepositories(basePackages = "com.example.SearchMicroservice.DB.Repository")
@SpringBootApplication()
@EnableScheduling
@EnableDiscoveryClient
public class SearchMicroserviceApplication {

	@Autowired
	private InitialMapper initialMapper;

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	private MovieRepository movieRepository;

	public static void main(String[] args) {
		SpringApplication.run(SearchMicroserviceApplication.class, args);
	}

	@PreDestroy
	public void deleteIndex() {
		elasticsearchOperations.indexOps(MovieES.class).delete();
	}

	@PostConstruct
	public void setLoader() {

		initialMapper.init();

	}
}
