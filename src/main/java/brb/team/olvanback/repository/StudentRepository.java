package brb.team.olvanback.repository;

import brb.team.olvanback.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Query("SELECT o.id AS orgId, COUNT(s.id) AS cnt " +
            "FROM olvan_students s JOIN s.organizations o " +
            "WHERE o.id IN :orgIds GROUP BY o.id")
    List<OrgCountProjection> findStudentCountsByOrgIds(@Param("orgIds") List<Long> orgIds);

    // Mapper method to get Map<Long, Integer>
    default Map<Long, Integer> countStudentsByOrgIds(List<Long> orgIds) {
        return findStudentCountsByOrgIds(orgIds).stream()
                .collect(Collectors.toMap(OrgCountProjection::getOrgId, p -> p.getCnt().intValue()));
    }
}
