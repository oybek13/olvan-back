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
public class OrganizationRequest {
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String directorPhoneNumber;
    private String directorFullName;
    private LocalDate dateBegin;
    private String address;
    private String inn;
    private Boolean isActive;
}
