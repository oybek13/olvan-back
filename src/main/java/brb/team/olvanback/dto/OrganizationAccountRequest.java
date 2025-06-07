package brb.team.olvanback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationAccountRequest {
    private String fullName;
    private String directorFullName;
    private String phoneNumber;
    private String username;
    private String address;
    private String inn;
}
