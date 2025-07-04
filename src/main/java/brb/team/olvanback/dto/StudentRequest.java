package brb.team.olvanback.dto;

import brb.team.olvanback.enums.EnrollType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentRequest {
    private String username;
    private String password;
    private String fullName;
    private String parentsFullName;
    private String pupilPhoneNumber;
    private String parentsPhoneNumber;
    private EnrollType enrollType;
    private LocalDate dateBegin;
    private Boolean status;
    private Integer attendance;
    private List<String> courseType;
    private List<String> teacherName;
}
