package no.nav.jobsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.nav.jobsearch.client.dto.Listing;
import no.nav.jobsearch.client.dto.JobList;
import no.nav.jobsearch.data.StatsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.EnableWireMock;

import java.time.LocalDate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@EnableWireMock
class FullIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void fetchJobsUntil() {
        buildMockResponse();

        ResponseEntity<StatsResponse> response = testRestTemplate.
                getForEntity("/api/stats", StatsResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        StatsResponse body = response.getBody();
        assertThat(body.getWeeks().size()).isEqualTo(1);
    }

    @SneakyThrows
    private void  buildMockResponse() {
       Listing content = Listing.builder()
                .description("Foo JAva, koTLin BAR")
               .published(LocalDate.now().toString())
                .build();
        stubFor(get(urlPathEqualTo("/public-feed/api/v1/ads"))
                .withHeader("Authorization", containing("Bearer"))
                .withQueryParam("page", equalTo("0"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(
                                JobList.builder()
                                        .content(List.of(content))
                                        .totalElements(1)
                                        .first(true)
                                        .last(true)
                                        .pageNumber(1)
                                        .pageSize(1)
                                        .build()
                        ))
                )
        );
    }

}
