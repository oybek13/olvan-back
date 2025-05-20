package brb.team.olvanback.repository;

import brb.team.olvanback.entity.User;
import brb.team.olvanback.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndRoleIn(Long id, List<UserRole> roles);

    Optional<User> findByIdAndRole(Long id, UserRole role);
}
