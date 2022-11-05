package pl.jakubsobolewski.Multiplex.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository
        extends JpaRepository<Room, Integer> {

    Room findRoomById(Integer Id);
}
