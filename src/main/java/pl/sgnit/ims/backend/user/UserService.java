package pl.sgnit.ims.backend.user;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> filterList(Map<String, String> conditions) {
        return Collections.emptyList();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> doUserQuery(String where) {
        String sql = "select u.* from users u where " + where;
        Query userQuery = entityManager.createNativeQuery(sql);
        List result = userQuery.getResultList();

        return (List<User>) result.stream()
            .map(row -> createUser((Object[]) row))
            .collect(Collectors.toList());
    }

    private User createUser(Object[] properties) {
        User user = new User();

        user.setId(((BigInteger)properties[0]).longValue());
        user.setCreated(properties[1]==null ? null : ((Timestamp)properties[1]).toLocalDateTime());
        user.setUpdated(properties[2]==null ? null : ((Timestamp)properties[2]).toLocalDateTime());
        user.setVersion(properties[3]==null ? null : (Integer)properties[3]);
        user.setPassword((String)properties[4]);
        user.setSalt((String)properties[5]);
        user.setUsername((String)properties[6]);
        user.setEmail((String)properties[7]);
        user.setFirstName((String)properties[8]);
        user.setLastName((String)properties[9]);
        return user;
    }

// Uncomment this function before first run to create first user
//    @EventListener(ApplicationReadyEvent.class)
//    public void createAminUser() {
//        User user = new User("admin", "admin1234");
//
//        userRepository.save(user);
//    }
}