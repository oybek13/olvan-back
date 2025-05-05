package brb.team.olvanback.dto;

import brb.team.olvanback.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationRequest {
    private String username;
    private String password;
    private String fullName;
    private String inn;
    private UserRole role;
    private boolean isActive;
}
