package brb.team.olvanback.specs;

import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class OrgSpecification {

    public static Specification<User> hasRoleIn(String role) {
        return (root, query, cb) -> {
            if (role == null || role.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("role"), role);
        };
    }
    public static Specification<User> hasAddress(String address) {
        return (root, query, cb) -> {
            if (address == null || address.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
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

    public static Specification<User> hasInn(String inn) {
        return (root, query, cb) -> {
            if (inn == null || inn.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("inn"), inn);
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
}
