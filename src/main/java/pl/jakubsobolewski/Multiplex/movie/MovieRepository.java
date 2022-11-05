package pl.jakubsobolewski.Multiplex.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository
        extends JpaRepository<Movie, Integer> {

    Movie findMovieById(Integer Id);
}
