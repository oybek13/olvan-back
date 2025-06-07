package brb.team.olvanback.repository;

import brb.team.olvanback.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
}
