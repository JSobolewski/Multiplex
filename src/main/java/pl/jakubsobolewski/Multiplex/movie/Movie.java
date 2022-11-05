package pl.jakubsobolewski.Multiplex.movie;

import pl.jakubsobolewski.Multiplex.screening.Screening;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @SequenceGenerator(
            name = "movie_sequence",
            sequenceName = "movie_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movie_sequence"
    )
    @Column(name = "movie_id", unique = true, nullable = false)
    private Integer id;
    private String title;

    @OneToMany(targetEntity = Screening.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "movie")
    private Set<Screening> screenings;

    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public Movie(String title) {
        this.title = title;
    }
}
