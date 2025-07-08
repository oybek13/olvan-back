package brb.team.olvanback.dto;

import brb.team.olvanback.enums.EnrollType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterClientDto {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String address;
    private EnrollType enrollType;
    private String bio;
}
