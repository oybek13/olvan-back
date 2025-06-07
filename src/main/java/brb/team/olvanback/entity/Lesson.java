package brb.team.olvanback.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "olvan_lessons")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    LocalDateTime createdAt;

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
    private String dateBegin;

    @Column(name = "days", nullable = false)
    private String days;

    @Column(name = "time_begin", nullable = false)
    private String timeBegin;

    @Column(name = "time_end", nullable = false)
    private String timeEnd;
}
