package pl.jakubsobolewski.Multiplex.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm") LocalDateTime screeningTime1,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd_HH:mm") LocalDateTime screeningTime2) {

        return reservationService.listScreeningsBetween(screeningTime1, screeningTime2);
    }

    @GetMapping
    @RequestMapping(path = "api/getScreeningInfo")
    public String getInfoAboutScreening(
            @RequestParam
            Integer chosenScreeningId
    ) {
        return reservationService.getInfoAboutScreening(chosenScreeningId);
    }

    @PostMapping("api/registerReservation")
    public String registerNewReservation(
            @RequestBody FreshReservation freshReservation) throws Exception {
        return reservationService.registerNewReservation(freshReservation);
    }
}
