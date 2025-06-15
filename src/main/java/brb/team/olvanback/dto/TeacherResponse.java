package brb.team.olvanback.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherResponse implements Serializable {
    private String username;
    private String fullName;
    private String degree;
    private String phoneNumber;
    private String gender;
    private String email;
    private String dateBegin;
    private Boolean status;
    private BigDecimal experience;
    private List<String> courseType;
    private Integer studentCount;
    private String cardPan;
    private String cardExpiry;
}
