package pl.jakubsobolewski.Multiplex.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubsobolewski.Multiplex.movie.Movie;
import pl.jakubsobolewski.Multiplex.movie.MovieRepository;
import pl.jakubsobolewski.Multiplex.multiplexUser.MultiplexUser;
import pl.jakubsobolewski.Multiplex.room.Room;
import pl.jakubsobolewski.Multiplex.room.RoomRepository;
import pl.jakubsobolewski.Multiplex.screening.Screening;
import pl.jakubsobolewski.Multiplex.screening.ScreeningComparator;
import pl.jakubsobolewski.Multiplex.screening.ScreeningRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Autowired
    ScreeningRepository screeningRepository;

    @Autowired
    MovieRepository movieRepository;

    public List<Screening> getScreeningsByScreeningTimes(LocalDateTime screeningTime1, LocalDateTime screeningTime2) {

        List<Screening> foundScreenings = new ArrayList<>();
        List<Screening> screeningList = screeningRepository.findAll();

        for(Screening screening : screeningList) {
            if((screening.getScreeningStartTime().isAfter(screeningTime1) || screening.getScreeningStartTime().isEqual(screeningTime1))
                && (screening.getScreeningStartTime().isBefore(screeningTime2) || screening.getScreeningStartTime().isEqual(screeningTime2))) {
                Movie movie = screening.getMovie();
                if(movie != null)
                    foundScreenings.add(screening);
            }
        }

        ScreeningComparator screeningComparator = new ScreeningComparator(screeningRepository);
        foundScreenings.sort(screeningComparator);

        return foundScreenings;
    }

    @Autowired
    RoomRepository roomRepository;

    public void addNewReservation(char[][] seatsArrayWithChosenSeats, MultiplexUser multiplexUser, Integer chosenScreeningId) {
        Screening chosenScreening = screeningRepository.findScreeningById(chosenScreeningId);

        Room reservationRoom = chosenScreening.getRoom();

        reservationRoom.setSeatsRow1(seatsArrayWithChosenSeats[0]);
        reservationRoom.setSeatsRow2(seatsArrayWithChosenSeats[1]);
        reservationRoom.setSeatsRow3(seatsArrayWithChosenSeats[2]);
        reservationRoom.setSeatsRow4(seatsArrayWithChosenSeats[3]);
        reservationRoom.setSeatsRow5(seatsArrayWithChosenSeats[4]);

        Reservation newReservation = new Reservation(reservationRoom, multiplexUser, chosenScreening);
        reservationRepository.save(newReservation);
    }
}
