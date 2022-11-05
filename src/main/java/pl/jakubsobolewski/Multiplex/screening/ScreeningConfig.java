package pl.jakubsobolewski.Multiplex.screening;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubsobolewski.Multiplex.movie.MovieRepository;
import pl.jakubsobolewski.Multiplex.room.RoomRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Configuration
public class ScreeningConfig {

    @Bean
    CommandLineRunner commandLineRunner_Screenings(
            ScreeningRepository repository, MovieRepository movieRepository, RoomRepository roomRepository) {
        return args -> {
            Screening screening1 = new Screening(
                    LocalDateTime.of(2022, Month.JANUARY, 11, 20, 0), LocalDateTime.of(2022, Month.JANUARY, 11, 21, 30),
                    movieRepository.findMovieById(1), roomRepository.findRoomById(3)
            );

            Screening screening2 = new Screening(
                    LocalDateTime.of(2023, Month.JANUARY, 11, 20, 45), LocalDateTime.of(2023, Month.JANUARY, 11, 21, 45),
                    movieRepository.findMovieById(2), roomRepository.findRoomById(1)
            );

            Screening screening3 = new Screening(
                    LocalDateTime.of(2023, Month.JANUARY, 12, 16, 0), LocalDateTime.of(2023, Month.JANUARY, 12, 17, 0),
                    movieRepository.findMovieById(3), roomRepository.findRoomById(2)
            );
            Screening screening4 = new Screening(
                    LocalDateTime.of(2023, Month.JANUARY, 12, 18, 0), LocalDateTime.of(2023, Month.JANUARY, 12, 19, 15),
                    movieRepository.findMovieById(3), roomRepository.findRoomById(2)
            );

            Screening screening5 = new Screening(
                    LocalDateTime.of(2023, Month.JANUARY, 13, 15, 0), LocalDateTime.of(2023, Month.JANUARY, 13, 17, 0),
                    movieRepository.findMovieById(3), roomRepository.findRoomById(3)
            );
            Screening screening6 = new Screening(
                    LocalDateTime.of(2023, Month.JANUARY, 13, 21, 30), LocalDateTime.of(2023, Month.JANUARY, 13, 22, 30),
                    movieRepository.findMovieById(1), roomRepository.findRoomById(3)
            );

            Screening screening7 = new Screening(
                    LocalDateTime.of(2023, Month.JANUARY, 11, 19, 0), LocalDateTime.of(2023, Month.JANUARY, 11, 20, 30),
                    movieRepository.findMovieById(1), roomRepository.findRoomById(1)
            );

            repository.saveAll(
                    List.of(screening1, screening2, screening3, screening4, screening5, screening6, screening7)
            );
        };
    }
}
