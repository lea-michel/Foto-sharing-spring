package local.epul4a.fotosharing.persistence.repository;

import local.epul4a.fotosharing.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    //check if user with an email already exists
    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEnabledTrue(String email);
}
