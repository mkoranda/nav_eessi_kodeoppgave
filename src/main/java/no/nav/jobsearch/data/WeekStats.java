package no.nav.jobsearch.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WeekStats {

    private int weekNumber;

    private int javaEntries;

    private int kotlinEntries;

    private int totalProcessedJobs;

}
