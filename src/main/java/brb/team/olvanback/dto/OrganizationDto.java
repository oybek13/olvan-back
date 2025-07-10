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
public class OrganizationDto {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private LocalDate dateBegin;
    private String address;
}
