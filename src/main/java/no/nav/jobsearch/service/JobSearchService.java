package no.nav.jobsearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.jobsearch.data.Week;
import no.nav.jobsearch.data.WeekStats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.previousOrSame;


@Slf4j
@Component
@RequiredArgsConstructor
public class JobSearchService {

    @Value("${app.numberOfWeeks:26}")
    private int numberOfWeeks;

    private final ProcessingService processingService;

    public List<WeekStats> fetchStats() {
        List<Week> weeks = buildListOfWeeks();

        CompletableFuture<List<WeekStats>> allFutures = CompletableFuture.allOf(
                weeks.stream()
                        .map(processingService::doProcess)
                        .toArray(CompletableFuture[]::new)
        ).thenApply(v -> weeks.stream()
                .map(processingService::doProcess)
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
        );

        // Wait for all futures to complete and return the results
        return allFutures.join();
    }

    private List<Week> buildListOfWeeks() {
        LocalDate startOfWeek = LocalDate.now().with(previousOrSame(DayOfWeek.SUNDAY));
        LocalDate startDate = startOfWeek.minusWeeks(numberOfWeeks - 1)
                .with(previousOrSame(DayOfWeek.MONDAY));
        return Stream.iterate(startDate, date -> date.plusWeeks(1))
                .limit(numberOfWeeks)
                .map(start -> {
                    LocalDate end = start.plusDays(6);
                    int weekNumber = start.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

                    return new Week(weekNumber, start, end);
                })
                .collect(Collectors.toList());
    }

}
