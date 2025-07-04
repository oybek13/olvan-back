package brb.team.olvanback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationsResponse {
    private Long id;
    private String fullName;
    private boolean isActive;
    private LocalDate dateBegin;
    private String address;
    private Integer studentCount;
    private Integer teacherCount;
}
