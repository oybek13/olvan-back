package brb.team.olvanback.repository;

import brb.team.olvanback.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
    Page<Lesson> findByOrgId(Long orgId, Pageable pageable);

}
