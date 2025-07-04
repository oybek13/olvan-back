package brb.team.olvanback.dto;

import brb.team.olvanback.entity.Teacher;
import brb.team.olvanback.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherInfoDto {
    private Teacher teacher;
    private User user;
}
