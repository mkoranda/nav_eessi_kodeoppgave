package no.nav.jobsearch.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Week {
    private int weekNumber;
    private LocalDate startDate;
    private LocalDate endDate;
}
