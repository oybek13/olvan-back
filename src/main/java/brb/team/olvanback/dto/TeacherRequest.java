package brb.team.olvanback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherRequest {
    private String username;
    private String password;
    private String fullName;
    private String degree;
    private String phoneNumber;
    private String gender;
    private String email;
    private String dateBegin;
    private Boolean status;
    private BigDecimal experience;
    private Integer studentCount;
    private List<String> courseType;
    private String cardPan;
    private String cardExpiry;
}
