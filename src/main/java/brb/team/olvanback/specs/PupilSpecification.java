package brb.team.olvanback.specs;

import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import org.springframework.data.jpa.domain.Specification;

public class PupilSpecification {

    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
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
}
