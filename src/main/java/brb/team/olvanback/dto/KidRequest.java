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
public class KidRequest {
    private String username;
    private String password;
    private String fullName;
    private String studentPhoneNumber;
    private LocalDate birthDate;
}
