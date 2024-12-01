package no.nav.jobsearch.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.jobsearch.client.dto.JobList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClient;


@Slf4j
@Component
@RequiredArgsConstructor
public class NavConsumer {

    @Value("${app.client.config.nav.fetchSize:50}")
    private String fetchSize;

    private final RestClient navRestClient;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public JobList fetchJobs(@PathVariable("formattedDateRange") String formattedDateRange,
                      @PathVariable("page") int page) {
        return navRestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/public-feed/api/v1/ads")
                        .queryParam("published", formattedDateRange)
                        .queryParam("page", page)
                        .queryParam("size", fetchSize)
                        .build())
                .retrieve()
                .body(JobList.class);
    }
}
