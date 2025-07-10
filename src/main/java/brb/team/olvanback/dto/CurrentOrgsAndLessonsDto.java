package brb.team.olvanback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentOrgsAndLessonsDto {
    private OrganizationDto organization;
    private List<LessonDto> lesson;
}
