package brb.team.olvanback.repository;

import brb.team.olvanback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<User> findById(Long id);
}
