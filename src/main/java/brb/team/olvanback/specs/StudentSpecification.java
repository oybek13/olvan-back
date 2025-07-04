package brb.team.olvanback.specs;

import brb.team.olvanback.entity.Student;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> hasStudentId(Long id) {
        return (root, query, cb) -> {
            if (id == null) return cb.conjunction();
            return cb.equal(root.get("id"), id);
        };
    }

    public static Specification<Student> isUserActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) return cb.conjunction();
            return cb.equal(root.get("user").get("isActive"), active);
        };
    }

    public static Specification<Student> hasUserFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
        };
    }

    public static Specification<Student> hasCourseTypes(List<String> courseTypes) {
        return (root, query, cb) -> {
            if (courseTypes == null || courseTypes.isEmpty()) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();
            for (String type : courseTypes) {
                predicates.add(cb.like(cb.lower(root.get("courseType")), "%" + type.toLowerCase() + "%"));
            }
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Student> hasOrgId(Long orgId) {
        return (root, query, cb) -> {
            if (orgId == null) return cb.conjunction();
            Join<Object, Object> orgJoin = root.join("organizations");
            return cb.equal(orgJoin.get("id"), orgId);
        };
    }
}


