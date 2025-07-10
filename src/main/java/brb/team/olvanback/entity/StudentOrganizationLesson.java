package brb.team.olvanback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "student_organization_lesson")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentOrganizationLesson extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @ManyToOne
    private Lesson lesson;
}
