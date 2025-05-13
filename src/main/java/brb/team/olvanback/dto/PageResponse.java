package brb.team.olvanback.dto;

import brb.team.olvanback.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse {
    private int page;
    private int size;
    private long totalElements;
    private List<OrganizationResponse> contents;
}
