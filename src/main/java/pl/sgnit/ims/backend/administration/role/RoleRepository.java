package pl.sgnit.ims.backend.administration.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByName(String name);

    List<Role> findAllByNameContaining(String name);
}
