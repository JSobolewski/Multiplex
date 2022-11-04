package pl.jakubsobolewski.Multiplex.reservation;

import pl.jakubsobolewski.Multiplex.multiplexUser.MultiplexUser;

public class FreshReservation {

    private final char[][] seatsArrayWithChosenSeats;
    private final MultiplexUser multiplexUser;

    public FreshReservation(char[][] seatsArrayWithChosenSeats, MultiplexUser multiplexUser) {
        this.seatsArrayWithChosenSeats = seatsArrayWithChosenSeats;
        this.multiplexUser = multiplexUser;
    }

    public char[][] getSeatsArrayWithChosenSeats() {
        return seatsArrayWithChosenSeats;
    }

    public MultiplexUser getMultiplexUser() {
        return multiplexUser;
    }
}
