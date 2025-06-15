package brb.team.olvanback.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherAccountRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
}
