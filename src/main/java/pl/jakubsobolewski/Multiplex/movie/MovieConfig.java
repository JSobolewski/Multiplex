package pl.jakubsobolewski.Multiplex.movie;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MovieConfig {

    @Bean
    CommandLineRunner commandLineRunner_Movies(
            MovieRepository repository) {
        return args -> {
            Movie movie1 = new Movie("Titanic");

            Movie movie2 = new Movie("Zielona mila");

            Movie movie3 = new Movie("Piła");

            repository.saveAll(
                    List.of(movie1, movie2, movie3)
            );
        };
    }
}
