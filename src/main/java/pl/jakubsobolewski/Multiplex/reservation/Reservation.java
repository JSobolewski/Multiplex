package pl.jakubsobolewski.Multiplex.reservation;

import pl.jakubsobolewski.Multiplex.multiplexUser.MultiplexUser;
import pl.jakubsobolewski.Multiplex.room.Room;
import pl.jakubsobolewski.Multiplex.screening.Screening;

import javax.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @SequenceGenerator(
            name = "reservation_sequence",
            sequenceName = "reservation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservation_sequence"
    )
    @Column(name = "reservation_id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(targetEntity = Room.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(targetEntity = MultiplexUser.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private MultiplexUser multiplexUser;

    @ManyToOne(targetEntity = Screening.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    public Reservation() {
    }

    public Reservation(Room room, MultiplexUser multiplexUser, Screening screening) {
        this.room = room;
        this.multiplexUser = multiplexUser;
        this.screening = screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public Screening getScreening() {
        return screening;
    }
}
