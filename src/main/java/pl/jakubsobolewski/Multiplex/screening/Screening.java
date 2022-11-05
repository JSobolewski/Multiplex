package pl.jakubsobolewski.Multiplex.screening;

import pl.jakubsobolewski.Multiplex.movie.Movie;
import pl.jakubsobolewski.Multiplex.reservation.Reservation;
import pl.jakubsobolewski.Multiplex.room.Room;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "screenings")
public class Screening {

    @Id
    @SequenceGenerator(
            name = "screening_sequence",
            sequenceName = "screening_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "screening_sequence"
    )
    @Column(name = "screening_id", unique = true, nullable = false)
    private Integer id;

    private LocalDateTime screeningStartTime;
    private LocalDateTime screeningEndTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(targetEntity = Reservation.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "screening")
    private Set<Reservation> reservationList;

    public Room getRoom() {
        return room;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screening() {
    }

    public Screening(LocalDateTime screeningStartTime,
                     LocalDateTime screeningEndTime,
                     Movie movie,
                     Room room) {
        this.screeningStartTime = screeningStartTime;
        this.screeningEndTime = screeningEndTime;
        this.movie = movie;
        this.room = room;
    }

    public LocalDateTime getScreeningStartTime() {
        return screeningStartTime;
    }

    public LocalDateTime getScreeningEndTime() {
        return screeningEndTime;
    }
}
