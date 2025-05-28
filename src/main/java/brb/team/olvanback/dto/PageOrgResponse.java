package brb.team.olvanback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageOrgResponse {
    private int page;
    private int size;
    private long totalElements;
    private List<OrganizationsResponse> contents;
}
