package pl.sgnit.ims.backend.administration.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndActive(String username, Boolean active);

    Optional<User> findByCode(String code);

    Optional<User> findByEmail(String email);
}
