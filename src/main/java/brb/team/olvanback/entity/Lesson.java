package brb.team.olvanback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "olvan_lessons")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Lesson extends BaseEntity {

    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "age_category", nullable = false)
    private String ageCategory;

    @Column(name = "pupil_count", nullable = false)
    private Integer pupilCount;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "teacher_full_name", nullable = false)
    private String teacherFullName;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "date_begin", nullable = false)
    private LocalDate dateBegin;

    @Column(name = "days", nullable = false)
    private String days;

    @Column(name = "time_begin", nullable = false)
    private String timeBegin;

    @Column(name = "time_end", nullable = false)
    private String timeEnd;
}
