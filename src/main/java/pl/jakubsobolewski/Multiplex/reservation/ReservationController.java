package pl.jakubsobolewski.Multiplex.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import pl.jakubsobolewski.Multiplex.multiplexUser.MultiplexUser;
import pl.jakubsobolewski.Multiplex.multiplexUser.MultiplexUserRepository;
import pl.jakubsobolewski.Multiplex.multiplexUser.MultiplexUserType;
import pl.jakubsobolewski.Multiplex.screening.Screening;
import pl.jakubsobolewski.Multiplex.screening.ScreeningRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @RequestMapping(path = "api/listScreeningsBetween")
    public String listScreeningsBetween(
            @RequestParam(required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm") LocalDateTime screeningTime1,

            @RequestParam(required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm") LocalDateTime screeningTime2) {

        StringBuilder result = new StringBuilder();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // We assume that no movie ends after midnight.
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");

        result.append("\r\n");
        for(Screening screening : reservationService.getScreeningsByScreeningTimes(screeningTime1, screeningTime2)) {
            result.append(screening.getMovie().getTitle() +
                    " | " +
                    screening.getScreeningStartTime().format(formatter1) +
                    "-" +
                    screening.getScreeningEndTime().format(formatter2) +
                    "\r\n"
            );
        }

        result.append("\r\n");
        return result.toString();
    }

    @Autowired
    ScreeningRepository screeningRepository;

    private Integer chosenScreeningId;

    private int chosenSeatsCount = 0;

    @GetMapping
    @RequestMapping(path = "api/getScreeningInfo")
    public String getInfoAboutScreening(
            @RequestParam(required = true)
            Integer chosenScreeningId
    ) {

        this.chosenScreeningId = chosenScreeningId;
        Screening chosenScreening = screeningRepository.findScreeningById(chosenScreeningId);
        StringBuilder result = new StringBuilder("Room id: " +
                    chosenScreening.getRoom().getId() +
                    "\r\n" +
                    "Movie: " +
                    chosenScreening.getMovie().getTitle() +
                    "\r\nSeats ('x' = busy, 'o' = free):\r\n");
        for(int j = 0; j < 5; j++) {
            if(chosenScreening.getRoom().getSeatsRow1()[j] == 'o') {
                result.append("o");
            } else {
                result.append("x");
            }
        }
        result.append("\r\n");

        for(int j = 0; j < 5; j++) {
            if(chosenScreening.getRoom().getSeatsRow2()[j] == 'o') {
                result.append("o");
            } else {
                result.append("x");
            }
        }
        result.append("\r\n");

        for(int j = 0; j < 5; j++) {
            if(chosenScreening.getRoom().getSeatsRow3()[j] == 'o') {
                result.append("o");
            } else {
                result.append("x");
            }
        }
        result.append("\r\n");

        for(int j = 0; j < 5; j++) {
            if(chosenScreening.getRoom().getSeatsRow4()[j] == 'o') {
                result.append("o");
            } else {
                result.append("x");
            }
        }
        result.append("\r\n");

        for(int j = 0; j < 5; j++) {
            if(chosenScreening.getRoom().getSeatsRow5()[j] == 'o') {
                result.append("o");
            } else {
                result.append("x");
            }
        }
        result.append("\r\n\r\n");

        return result.toString();
    }

    @Autowired
    MultiplexUserRepository multiplexUserRepository;

    @PostMapping("api/registerReservation")
    public String registerNewReservation(
            @RequestBody FreshReservation freshReservation) throws Exception {

        char[][] seatsArrayWithChosenSeats = freshReservation.getSeatsArrayWithChosenSeats();
        MultiplexUser multiplexUser = freshReservation.getMultiplexUser();

        if(isNameCorrect(multiplexUser.getName(), multiplexUser.getSurname()) == false) {
            throw new Exception("Provided user name and/or surname incorrect! " +
                    "Correct name and surname should be at least 3 characters long, starting with capital letter. " +
                    "If the surname consists of 2 parts (then they should be separated with a single dash), the second part should also start with capital letter."
            );
        }

        Screening chosenScreening = screeningRepository.findScreeningById(chosenScreeningId);
        if(chosenScreening == null) {
            throw new Exception("You didn't choose screening/chosen screening not found - You have to choose proper screening first!");
        } else {
            if (LocalDateTime.now().isBefore(chosenScreening.getScreeningStartTime().minusMinutes(15))) {
                char[][] seatsArrayBeforeChoice = {
                        chosenScreening.getRoom().getSeatsRow1(),
                        chosenScreening.getRoom().getSeatsRow2(),
                        chosenScreening.getRoom().getSeatsRow3(),
                        chosenScreening.getRoom().getSeatsRow4(),
                        chosenScreening.getRoom().getSeatsRow5(),
                };

                chosenSeatsCount = 0;
                // Function checkSeatsCorrectness(char[][], char[][]) checks if the seats schema after booking reservation is correct, according to the assumptions - there cannot be a single place left over in a row between 2 already reserved places.
                // It also calculates count of chosen seats (chosenSeatsCount variable).
                // It's also responsible for checking if chosen seats are already busy, if given seats schema is incorrect, if user didn't choose any seats or if all of the seats in chosen screening's room are already busy.
                if(!checkSeatsCorrectness(seatsArrayWithChosenSeats, seatsArrayBeforeChoice)) {
                    throw new Exception("Cannot add new reservation! There cannot be a single place left over in a row between two already reserved places! " +
                            "Choose another seats and try to book reservation again!"
                    );
                }

                if (multiplexUserRepository.findMultiplexUserByNameAndSurname(multiplexUser.getName(), multiplexUser.getSurname()) != null)
                    reservationService.addNewReservation(seatsArrayWithChosenSeats, multiplexUserRepository.findMultiplexUserByNameAndSurname(multiplexUser.getName(), multiplexUser.getSurname()), chosenScreeningId);
                else
                    reservationService.addNewReservation(seatsArrayWithChosenSeats, multiplexUser, chosenScreeningId);

                StringBuilder result = new StringBuilder("Total amount to pay: ");

                // We assume reservation expires 5 minutes before screening starts.
                // If the user won't appear at latest 5 minutes before screening starts - the ticket is lost.
                LocalDateTime reservationExpirationTime = chosenScreening.getScreeningStartTime().minusMinutes(5);
                float totalCost = 0f;

                // For one reservation (ticket) there can be multiple places, but we assume they're all one type (for Student/Adult/Child).
                if (multiplexUser.getUserType() == MultiplexUserType.ADULT) {
                    totalCost = chosenSeatsCount * 25;
                } else if (multiplexUser.getUserType() == MultiplexUserType.STUDENT) {
                    totalCost = chosenSeatsCount * 18;
                } else if (multiplexUser.getUserType() == MultiplexUserType.CHILD) {
                    totalCost = chosenSeatsCount * 12.5f;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                result.append(totalCost + " PLN\r\nReservation expiration time: " + formatter.format(reservationExpirationTime));
                result.append("\r\n\r\n");

                return result.toString();
            } else {
                throw new Exception("Seats can be booked at latest 15 minutes before the screening begins, you're too late!");
            }
        }
    }

    public boolean isNameCorrect(String name, String surname) {
        boolean result = false;
        if(name.length() >= 3 && surname.length() >= 3) {
            if(Character.isUpperCase(name.charAt(0)) && Character.isUpperCase(surname.charAt(0))) {
                if(surname.contains("-")) {
                    Pattern pattern = Pattern.compile("-");
                    Matcher matcher = pattern.matcher(surname);
                    int dashesCount = 0;
                    int dashIndex = -1;

                    while(matcher.find()) {
                        dashesCount++;
                        dashIndex = matcher.start();
                    }
                    if(dashesCount == 1) {
                        if(dashIndex != 0 && dashIndex != surname.length()-1) {
                            if(Character.isUpperCase(surname.charAt(dashIndex+1)))
                                result = true;
                        }
                    }
                } else {
                    result = true;
                }
            }
        }

        return result;
    }

    public boolean checkSeatsCorrectness(char[][] seatsArrayWithChosenSeats, char[][] seatsArrayBeforeChoice) throws Exception {
        char[][] potentialSeatsArrayAfterReservation = new char[5][5];
        int alreadyBusySeats = 0;

        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                potentialSeatsArrayAfterReservation[i][j] = 'o';

                if(seatsArrayBeforeChoice[i][j] == 'x')
                    alreadyBusySeats++;

                if(seatsArrayBeforeChoice[i][j] == 'o' && seatsArrayWithChosenSeats[i][j] == 'x') {
                    potentialSeatsArrayAfterReservation[i][j] = 'x';
                    chosenSeatsCount++;
                } else if(seatsArrayBeforeChoice[i][j] == 'x' && seatsArrayWithChosenSeats[i][j] == 'x')
                    throw new Exception("One or more of chosen seats are already busy! Change seats and try to book reservation again!");

                if(seatsArrayBeforeChoice[i][j] == 'x' && seatsArrayWithChosenSeats[i][j] == 'o') {
                    throw new Exception(
                            "Wrong schema of seats given during booking the reservation - one or more given seats during booking are free, but they have been already reserved! " +
                            "Give correct seats schema and try again!"
                    );
                } else if(seatsArrayBeforeChoice[i][j] == 'o' && seatsArrayWithChosenSeats[i][j] == 'o') {
                    potentialSeatsArrayAfterReservation[i][j] = 'o';
                }
            }
        }

        if(chosenSeatsCount == 0)
            throw new Exception("You didn't choose any seats! Choose some seats and try to book reservation again!");

        if(alreadyBusySeats == 25)
            throw new Exception("All of the seats in a chosen room are already busy! You can't book a reservation for this screening!");

        String singlePlaceLeftOverMessage = "There is a single place left over in a row between 2 already reserved places! It's the situation we have to avoid. " +
                "Please choose another seats and try to book reservation again!";

        for(int i = 1; i < 4; i++) {
            for(int j = 1; j < 4; j++) {
                if(potentialSeatsArrayAfterReservation[i][j] == 'x') {
                    //checking to the right
                    if(j+2 < 5) {
                        if (potentialSeatsArrayAfterReservation[i][j+1] == 'o' && potentialSeatsArrayAfterReservation[i][j+2] == 'x') {
                            throw new Exception(singlePlaceLeftOverMessage);
                        }
                    }
                    //checking to the bottom
                    if(i+2 < 5) {
                        if (potentialSeatsArrayAfterReservation[i+1][j] == 'o' && potentialSeatsArrayAfterReservation[i+2][j] == 'x') {
                            throw new Exception(singlePlaceLeftOverMessage);
                        }
                    }
                    //checking to the top
                    if(i-2 >= 0) {
                        if (potentialSeatsArrayAfterReservation[i-1][j] == 'o' && potentialSeatsArrayAfterReservation[i-2][j] == 'x') {
                            throw new Exception(singlePlaceLeftOverMessage);
                        }
                    }
                    //checking to the left
                    if(j-2 >= 0) {
                        if (potentialSeatsArrayAfterReservation[i][j-1] == 'o' && potentialSeatsArrayAfterReservation[i][j-2] == 'x') {
                            throw new Exception(singlePlaceLeftOverMessage);
                        }
                    }
                } else if(potentialSeatsArrayAfterReservation[i][j] == 'o'){
                    //checking to the left and right
                    if (potentialSeatsArrayAfterReservation[i][j+1] == 'x' && potentialSeatsArrayAfterReservation[i][j-1] == 'x') {
                        throw new Exception(singlePlaceLeftOverMessage);
                    }
                    //checking to the top and bottom
                    if (potentialSeatsArrayAfterReservation[i-1][j] == 'x' && potentialSeatsArrayAfterReservation[i+1][j] == 'x') {
                        throw new Exception(singlePlaceLeftOverMessage);
                    }
                }
            }
        }

        //checking edges
        for(int j = 0; j < 3; j++) {
            //checking to the right (top edge)
            if(potentialSeatsArrayAfterReservation[0][j] == 'x') {
                if(potentialSeatsArrayAfterReservation[0][j+1] == 'o' && potentialSeatsArrayAfterReservation[0][j+2] == 'x') {
                    throw new Exception(singlePlaceLeftOverMessage);
                }
            }
            //checking to the right (bottom edge)
            if(potentialSeatsArrayAfterReservation[4][j] == 'x') {
                if(potentialSeatsArrayAfterReservation[4][j+1] == 'o' && potentialSeatsArrayAfterReservation[4][j+2] == 'x') {
                    throw new Exception(singlePlaceLeftOverMessage);
                }
            }
        }
        for(int i = 0; i < 3; i++) {
            //checking to the bottom (left edge)
            if(potentialSeatsArrayAfterReservation[i][0] == 'x') {
                if(potentialSeatsArrayAfterReservation[i+1][0] == 'o' && potentialSeatsArrayAfterReservation[i+2][0] == 'x') {
                    throw new Exception(singlePlaceLeftOverMessage);
                }
            }
            //checking to the bottom (right edge)
            if(potentialSeatsArrayAfterReservation[i][4] == 'x') {
                if(potentialSeatsArrayAfterReservation[i+1][4] == 'o' && potentialSeatsArrayAfterReservation[i+2][4] == 'x') {
                    throw new Exception(singlePlaceLeftOverMessage);
                }
            }
        }
        for(int j = 1; j < 4; j++) {
            //checking to the left and right (top edge)
            if(potentialSeatsArrayAfterReservation[0][j] == 'o') {
                if(potentialSeatsArrayAfterReservation[0][j-1] == 'x' && potentialSeatsArrayAfterReservation[0][j+1] == 'x') {
                    throw new Exception(singlePlaceLeftOverMessage);
                }
            }
            //checking to the left and right (bottom edge)
            if(potentialSeatsArrayAfterReservation[4][j] == 'o') {
                if(potentialSeatsArrayAfterReservation[4][j-1] == 'x' && potentialSeatsArrayAfterReservation[4][j+1] == 'x') {
                    throw new Exception(singlePlaceLeftOverMessage);
                }
            }
        }
        for(int i = 1; i < 4; i++) {
            //checking to the top and bottom (left edge)
            if(potentialSeatsArrayAfterReservation[i][0] == 'o') {
                if(potentialSeatsArrayAfterReservation[i-1][0] == 'x' && potentialSeatsArrayAfterReservation[i+1][0] == 'x') {
                    throw new Exception(singlePlaceLeftOverMessage);
                }
            }
            //checking to the top and bottom (right edge)
            if(potentialSeatsArrayAfterReservation[i][4] == 'o') {
                if(potentialSeatsArrayAfterReservation[i-1][4] == 'x' && potentialSeatsArrayAfterReservation[i+1][4] == 'x') {
                    throw new Exception(singlePlaceLeftOverMessage);
                }
            }
        }

        return true;
    }
}
