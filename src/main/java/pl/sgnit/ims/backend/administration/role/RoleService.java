package pl.sgnit.ims.backend.administration.role;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public List<Role> findAll(String name) {
        if (name == null || name.isEmpty()) {
            return roleRepository.findAll();
        }
        return roleRepository.findAllByName(name);
    }

    public List<Role> filterAllByName(String name) {
        return roleRepository.findAllByNameContaining(name);
    }

}
