package brb.team.olvanback.repository;

import brb.team.olvanback.entity.Lesson;
import brb.team.olvanback.entity.Organization;
import brb.team.olvanback.entity.Student;
import brb.team.olvanback.entity.StudentOrganizationLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentOrganizationLessonRepository extends JpaRepository<StudentOrganizationLesson, Long> {
    boolean existsByStudentAndOrganizationAndLesson(Student student, Organization organization, Lesson lesson);
    List<StudentOrganizationLesson> findByStudentIdAndOrganizationId(Long studentId, Long orgId);
    List<StudentOrganizationLesson> findByStudentId(Long studentId);
}

