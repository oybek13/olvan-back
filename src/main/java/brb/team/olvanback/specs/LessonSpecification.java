package brb.team.olvanback.specs;

import brb.team.olvanback.entity.Lesson;
import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import org.springframework.data.jpa.domain.Specification;

public class LessonSpecification {
    public static Specification<Lesson> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
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

    public static Specification<Lesson> status(Boolean status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Lesson> hasOrgId(Long orgId) {
        return (root, query, cb) -> {
            if (orgId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("orgId"), orgId);
        };
    }

    public static Specification<Lesson> hasId(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("id"), id);
        };
    }

    public static Specification<Lesson> hasTeacherFullName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("teacherFullName")), "%" + name.toLowerCase() + "%");
        };
    }
}
