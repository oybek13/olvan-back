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
public class PupilResponse {
    private Long id;
    private String username;
    private String fullName;
    private String parentsFullName;
    private String pupilPhoneNumber;
    private String parentsPhoneNumber;
    private EnrollType enrollType;
    private LocalDate dateBegin;
    private List<String> courseType;
    private int attendance;
    private Boolean status;
    private List<String> teacherName;
}
