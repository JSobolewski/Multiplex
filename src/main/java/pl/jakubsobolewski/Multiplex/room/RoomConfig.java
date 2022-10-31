package pl.jakubsobolewski.Multiplex.room;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubsobolewski.Multiplex.screening.Screening;
import pl.jakubsobolewski.Multiplex.screening.ScreeningRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class RoomConfig {

    @Bean
    CommandLineRunner commandLineRunner_Rooms(
            RoomRepository repository, ScreeningRepository screeningRepository) {
        return args -> {
            List<Screening> screeningList1 = new ArrayList<>();
            List<Screening> screeningList2 = new ArrayList<>();
            List<Screening> screeningList3 = new ArrayList<>();

            Collections.addAll(screeningList1,
                    screeningRepository.findScreeningById(1), screeningRepository.findScreeningById(2)
            );
            Collections.addAll(screeningList2,
                    screeningRepository.findScreeningById(3), screeningRepository.findScreeningById(4)
            );
            Collections.addAll(screeningList3,
                    screeningRepository.findScreeningById(5), screeningRepository.findScreeningById(6), screeningRepository.findScreeningById(7)
            );

            char[] startArrayRow = new char[5];
            for(int i = 0; i < 5; i++)
                startArrayRow[i] = 'o';

            Room room1 = new Room(
                    screeningList1,
                    startArrayRow.clone(),
                    startArrayRow.clone(),
                    startArrayRow.clone(),
                    startArrayRow.clone(),
                    startArrayRow.clone()
            );

            Room room2 = new Room(
                    screeningList2,
                    startArrayRow.clone(),
                    startArrayRow.clone(),
                    startArrayRow.clone(),
                    startArrayRow.clone(),
                    startArrayRow.clone()
            );

            Room room3 = new Room(
                    screeningList3,
                    startArrayRow.clone(),
                    startArrayRow.clone(),
                    startArrayRow.clone(),
                    startArrayRow.clone(),
                    startArrayRow.clone()
            );

            repository.saveAll(
                    List.of(room1, room2, room3)
            );
        };

    }
}
