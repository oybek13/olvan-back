package brb.team.olvanback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonDto {
    private Long id;
    private String name;
    private String description;
    private String ageCategory;
    private Integer studentCount;
    private Long price;
    private String teacherFullName;
    private String days;
    private String timeBegin;
    private String timeEnd;
}
