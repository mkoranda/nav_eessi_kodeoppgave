package no.nav.jobsearch.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient navRestClient(
            RestClient.Builder restClientBuilder,
            @Value("${app.client.config.nav.url}") String uri,
            @Value("${app.client.config.nav.token}") String token) {

        return restClientBuilder
                .baseUrl(uri)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

}
