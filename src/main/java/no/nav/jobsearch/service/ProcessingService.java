package no.nav.jobsearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.jobsearch.client.dto.Listing;
import no.nav.jobsearch.client.dto.JobList;
import no.nav.jobsearch.client.NavConsumer;
import no.nav.jobsearch.data.Stats;
import no.nav.jobsearch.data.Week;
import no.nav.jobsearch.data.WeekStats;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static no.nav.jobsearch.utils.DateUtils.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessingService {

    private final NavConsumer navRestClient;

    @Async
    @Cacheable(value = "weekStatsCache", key = "#week.weekNumber")
    public CompletableFuture<WeekStats> doProcess(Week week) {
        String formattedDateRange = String.format("[%s,%s)", format(week.getStartDate()), format(week.getEndDate()));
        int page = 0;
        List<Stats> content = new ArrayList<>();

        while (true) {
            JobList jobList = processPage(week, formattedDateRange, page++);
            content.addAll(jobList.getListings().stream()
                    .map(this::fromListing)
                    .toList());

            if (jobList.isLast()) {
                break;
            }
        }

        return CompletableFuture.supplyAsync(() -> buildStats(week, content));
    }

    private WeekStats buildStats(Week week, List<Stats> allContent) {
        long javaCount = allContent.stream().filter(Stats::isHasJava).count();
        long kotlinCount = allContent.stream().filter(Stats::isHasKotlin).count();
        int totalProcessedJobs = allContent.size();

        return WeekStats.builder()
                .weekNumber(week.getWeekNumber())
                .javaEntries((int) javaCount)
                .kotlinEntries((int) kotlinCount)
                .totalProcessedJobs(totalProcessedJobs)
                .build();
    }

    private Stats fromListing(Listing content) {
        return new Stats(containsKeyword(content, "java"),
                containsKeyword(content, "kotlin"));
    }

    private boolean containsKeyword(Listing content, String keyword) {
        return content.getDescription().toLowerCase().contains(keyword);
    }

    private JobList processPage(Week week, String formattedDateRange, int page) {
        log.debug("Processing page {} for week {}", page, week.getWeekNumber());
        return navRestClient.fetchJobs(formattedDateRange, page);
    }

}
