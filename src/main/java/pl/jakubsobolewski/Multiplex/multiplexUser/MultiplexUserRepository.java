package pl.jakubsobolewski.Multiplex.multiplexUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiplexUserRepository
        extends JpaRepository<MultiplexUser, Integer> {

    MultiplexUser findMultiplexUserByNameAndSurname(String name, String surname);
}
