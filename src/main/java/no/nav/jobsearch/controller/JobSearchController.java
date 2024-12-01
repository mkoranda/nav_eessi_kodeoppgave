package no.nav.jobsearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.jobsearch.data.StatsResponse;
import no.nav.jobsearch.data.WeekStats;
import no.nav.jobsearch.service.JobSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api", produces = "application/json")
public class JobSearchController {

    private final JobSearchService jobSearchService;

    @GetMapping("/stats")
    public StatsResponse getStats() {
        LocalDateTime start = LocalDateTime.now();
        List<WeekStats> weeks = jobSearchService.fetchStats();
        long processingTime = ChronoUnit.SECONDS.between(start, LocalDateTime.now());

        return new StatsResponse(processingTime, weeks);
    }
}
