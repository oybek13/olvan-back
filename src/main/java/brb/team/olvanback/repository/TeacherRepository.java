package brb.team.olvanback.repository;

import brb.team.olvanback.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    Integer countByOrgId(Long orgId);

    Optional<Teacher> findByUserId(Long userId);

    Page<Teacher> findByOrgId(Long orgId, Pageable pageable);

    Page<Teacher> findByOrgIdAndFullNameContainingIgnoreCase(Long orgId, String fullName, Pageable pageable);
}
