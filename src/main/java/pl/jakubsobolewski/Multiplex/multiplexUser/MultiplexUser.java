package pl.jakubsobolewski.Multiplex.multiplexUser;

import pl.jakubsobolewski.Multiplex.reservation.Reservation;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "multiplex_users")
public class MultiplexUser {

    @Id
    @SequenceGenerator(
            name = "multiplexuser_sequence",
            sequenceName = "multiplexuser_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "multiplexuser_sequence"
    )
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer id;
    private String name;
    private String surname;

    @Enumerated(EnumType.STRING)
    private MultiplexUserType userType;

    @OneToMany(targetEntity = Reservation.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "multiplexUser")
    private List<Reservation> reservationList;

    public MultiplexUser() {
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public MultiplexUserType getUserType() {
        return userType;
    }
}
