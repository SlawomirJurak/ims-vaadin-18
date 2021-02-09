package pl.sgnit.ims.backend.administration.user;

import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;
import pl.sgnit.ims.backend.administration.role.Role;
import pl.sgnit.ims.views.about.AboutView;
import pl.sgnit.ims.views.calendar.weekscedule.WeekScheduleView;
import pl.sgnit.ims.views.logout.LogoutView;
import pl.sgnit.ims.views.main.MainView;
import pl.sgnit.ims.views.administration.role.RolesView;
import pl.sgnit.ims.views.administration.user.UsersView;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userService.findByUsernameAndActive(username, true);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.checkPassword(password)) {
                VaadinSession.getCurrent().setAttribute(User.class, user);
                createRoutes();
                return true;
            }
        }
        return false;
    }

    public void setPassword(String activationCode, String password) {
        Optional<User> optionalUser = userService.findByCode(activationCode);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.hashPassword(password);
            user.setActive(true);
            user.setCode(null);
            userService.save(user);
        }
    }

    private void createRoutes() {
        RouteConfiguration.forSessionScope().setRoute("about", AboutView.class, MainView.class);
        RouteConfiguration.forSessionScope().setRoute("logout", LogoutView.class);

        User user = VaadinSession.getCurrent().getAttribute(User.class);
        if (user.getAdministrator()) {
            createAllRoutes();
        } else {
            createGrantedRoutes(user.getGrantedRoles());
        }
    }

    private void createAllRoutes() {
        RouteConfiguration.forSessionScope().setRoute("roles", RolesView.class, MainView.class);
        RouteConfiguration.forSessionScope().setRoute("users", UsersView.class, MainView.class);
        RouteConfiguration.forSessionScope().setRoute("weekSchedule", WeekScheduleView.class, MainView.class);
    }

    private void createGrantedRoutes(Set<Role> grantedRoles) {
    }
}
