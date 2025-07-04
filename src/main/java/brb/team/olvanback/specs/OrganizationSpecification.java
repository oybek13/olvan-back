package brb.team.olvanback.specs;

import brb.team.olvanback.entity.Organization;
import brb.team.olvanback.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class OrganizationSpecification {

    public static Specification<Organization> hasFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
        };
    }

    public static Specification<Organization> hasAddress(String address) {
        return (root, query, cb) -> {
            if (address == null || address.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }

    public static Specification<Organization> hasInn(String inn) {
        return (root, query, cb) -> {
            if (inn == null || inn.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("innOrPinfl")), "%" + inn.toLowerCase() + "%");
        };
    }

    public static Specification<Organization> hasUserActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) return cb.conjunction();
            Join<Organization, User> userJoin = root.join("user");
            return cb.equal(userJoin.get("isActive"), active);
        };
    }
}


