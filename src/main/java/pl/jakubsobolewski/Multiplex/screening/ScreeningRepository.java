package pl.jakubsobolewski.Multiplex.screening;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningRepository
        extends JpaRepository<Screening, Integer> {

    Screening findScreeningById(Integer Id);
}
