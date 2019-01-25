package com.example.movieflixclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.movieflixclient.model.Movie;
import com.example.movieflixclient.model.MovieEvent;

@SpringBootApplication
public class MovieFlixClientApplication {

        @Bean
        WebClient client() {
            return WebClient.create("http://localhost:8090/movies");
        }
    
        @Bean
        CommandLineRunner demo(WebClient client) {
            return args -> {
                client.get()
                    .uri("")
                    .exchange()
                    .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Movie.class))
                    .filter(movie -> movie.getName().toLowerCase().contains("movie"))
                    .subscribe(movie -> 
                       client.get()
                            .uri("/{id}/events", movie.getId())
                            .exchange()
                            .flatMapMany(cr -> cr.bodyToFlux(MovieEvent.class))
                            .subscribe(System.out::println));
            };
        }
        
        
	public static void main(String[] args) {
		SpringApplication.run(MovieFlixClientApplication.class, args);
	}

}

