package pl.jakubsobolewski.Multiplex.screening;

import java.util.Comparator;

public class ScreeningComparator implements Comparator<Screening> {

    ScreeningRepository screeningRepository;

    public ScreeningComparator(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    @Override
    public int compare(Screening s1, Screening s2) {
        int comparisonValue = s1.getMovie().getTitle().compareToIgnoreCase(s2.getMovie().getTitle());
        if(comparisonValue == 0) {
            if(s1.getScreeningStartTime().isBefore(s2.getScreeningStartTime()))
                return -1;
            else if (s1.getScreeningStartTime().isEqual(s2.getScreeningStartTime()))
                return 0;
            else
                return 1;
        }

        return comparisonValue;
    }
}
