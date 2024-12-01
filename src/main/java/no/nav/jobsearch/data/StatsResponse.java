package no.nav.jobsearch.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatsResponse {

    private long totalProcessingTime;

    private List<WeekStats> weeks;
}
