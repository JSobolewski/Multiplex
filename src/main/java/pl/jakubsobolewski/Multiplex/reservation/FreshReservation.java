package pl.jakubsobolewski.Multiplex.reservation;

import pl.jakubsobolewski.Multiplex.multiplexUser.MultiplexUser;

public class FreshReservation {

    private final char[][] chosenSeatsArray;
    private final MultiplexUser multiplexUser;

    public FreshReservation(char[][] chosenSeatsArray, MultiplexUser multiplexUser) {
        this.chosenSeatsArray = chosenSeatsArray;
        this.multiplexUser = multiplexUser;
    }

    public char[][] getChosenSeatsArray() {
        return chosenSeatsArray;
    }

    public MultiplexUser getMultiplexUser() {
        return multiplexUser;
    }
}
