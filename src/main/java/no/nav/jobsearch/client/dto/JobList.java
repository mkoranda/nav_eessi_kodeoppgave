package no.nav.jobsearch.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class JobList {

    private List<Listing> content;

    private int totalElements;

    private int pageNumber;

    private int pageSize;

    private int totalPages;

    private boolean first;

    private boolean last;

    private String sort;
}
