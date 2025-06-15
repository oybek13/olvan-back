package brb.team.olvanback.dto;

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
public class LessonRequest {
    private String name;
    private String description;
    private String ageCategory;
    private Integer pupilCount;
    private Long price;
    private String teacherFullName;
    private Boolean status;
    private LocalDate dateBegin;
    private List<String> days;
    private String timeBegin;
    private String timeEnd;
}
