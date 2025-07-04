package brb.team.olvanback.specs;

import brb.team.olvanback.entity.Teacher;
import org.springframework.data.jpa.domain.Specification;

public class TeacherSpecification {

    public static Specification<Teacher> hasFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
        };
    }

    public static Specification<Teacher> hasPhoneNumber(String phoneNumber) {
        return (root, query, cb) -> {
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("phoneNumber")), "%" + phoneNumber.toLowerCase() + "%");
        };
    }

    public static Specification<Teacher> hasDateBegin(String dateBegin) {
        return (root, query, cb) -> {
            if (dateBegin == null || dateBegin.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("dateBegin")), "%" + dateBegin.toLowerCase() + "%");
        };
    }

    public static Specification<Teacher> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) return cb.conjunction();
            return cb.equal(root.join("user").get("isActive"), active);
        };
    }


    public static Specification<Teacher> hasOrgId(Long orgId) {
        return (root, query, cb) -> {
            if (orgId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("orgId"), orgId);
        };
    }

    public static Specification<Teacher> hasStudentCount(Integer studentCount) {
        return (root, query, cb) -> {
            if (studentCount == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("studentCount"), studentCount);
        };
    }
}