package brb.team.olvanback.specs;

import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PupilSpecification {

    public static Specification<User> hasDateBegin(String dateBegin) {
        return (root, query, cb) -> {
            if (dateBegin == null || dateBegin.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("dateBegin")), "%" + dateBegin.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasRole(UserRole role) {
        return (root, query, cb) -> {
            if (role == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("role"), role);
        };
    }

    public static Specification<User> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isActive"), active);
        };
    }

    public static Specification<User> hasOrgId(Long orgId) {
        return (root, query, cb) -> {
            if (orgId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("orgId"), orgId);
        };
    }

    public static Specification<User> hasId(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("id"), id);
        };
    }

    public static Specification<User> hasEnrollType(String enrollType) {
        return (root, query, cb) -> {
            if (enrollType == null || enrollType.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("enrollType")), "%" + enrollType.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasCourseTypes(List<String> courseTypes) {
        return (root, query, cb) -> {
            if (courseTypes == null || courseTypes.isEmpty()) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            for (String type : courseTypes) {
                predicates.add(cb.like(cb.lower(root.get("courseType")), "%" + type.toLowerCase() + "%"));
            }

            return cb.or(predicates.toArray(new Predicate[0])); // <--- AND emas, OR bo‘ldi!
        };
    }

}
