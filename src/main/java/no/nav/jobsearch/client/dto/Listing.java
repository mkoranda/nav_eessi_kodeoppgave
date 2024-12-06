package no.nav.jobsearch.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Listing {
    private String uuid;

    private String published;

    private String description;
}
