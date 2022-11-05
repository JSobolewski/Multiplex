package pl.jakubsobolewski.Multiplex.room;

import pl.jakubsobolewski.Multiplex.reservation.Reservation;
import pl.jakubsobolewski.Multiplex.screening.Screening;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @SequenceGenerator(
            name = "room_sequence",
            sequenceName = "room_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "room_sequence"
    )
    @Column(name = "room_id", unique = true, nullable = false)
    private Integer id;

    @OneToMany(targetEntity = Screening.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "room")
    private List<Screening> screeningList;

    // We assume that every room is 5 rows x 5 seats.
    // o o o o o  <seatsRow1>
    // o o o o o  <seatsRow2>
    // o o o o o  <seatsRow3>
    // o o o o o  <seatsRow4>
    // o o o o o  <seatsRow5>
    private char[] seatsRow1 = new char[5];
    private char[] seatsRow2 = new char[5];
    private char[] seatsRow3 = new char[5];
    private char[] seatsRow4 = new char[5];
    private char[] seatsRow5 = new char[5];

    @OneToMany(targetEntity = Reservation.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "room")
    private Set<Reservation> reservationList;

    public Room() {
    }

    public char[] getSeatsRow1() {
        return seatsRow1;
    }

    public void setSeatsRow1(char[] seatsRow1) {
        this.seatsRow1 = seatsRow1;
    }

    public char[] getSeatsRow2() {
        return seatsRow2;
    }

    public void setSeatsRow2(char[] seatsRow2) {
        this.seatsRow2 = seatsRow2;
    }

    public char[] getSeatsRow3() {
        return seatsRow3;
    }

    public void setSeatsRow3(char[] seatsRow3) {
        this.seatsRow3 = seatsRow3;
    }

    public char[] getSeatsRow4() {
        return seatsRow4;
    }

    public void setSeatsRow4(char[] seatsRow4) {
        this.seatsRow4 = seatsRow4;
    }

    public char[] getSeatsRow5() {
        return seatsRow5;
    }

    public void setSeatsRow5(char[] seatsRow5) {
        this.seatsRow5 = seatsRow5;
    }

    public Integer getId() {
        return id;
    }

    public Room(
            List<Screening> screeningList,
            char[] seatsRow1,
            char[] seatsRow2,
            char[] seatsRow3,
            char[] seatsRow4,
            char[] seatsRow5) {

        this.screeningList = screeningList;

        this.seatsRow1 = seatsRow1;
        this.seatsRow2 = seatsRow2;
        this.seatsRow3 = seatsRow3;
        this.seatsRow4 = seatsRow4;
        this.seatsRow5 = seatsRow5;
    }
}
