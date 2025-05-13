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
public class OrganizationResponse {
    private String username;
    private UserRole role;
    private String fullName;
    private boolean isActive;
    private String inn;
}
