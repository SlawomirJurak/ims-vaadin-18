package pl.sgnit.ims.backend.user;

import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;
import pl.sgnit.ims.views.about.AboutView;
import pl.sgnit.ims.views.logout.LogoutView;
import pl.sgnit.ims.views.main.MainView;
import pl.sgnit.ims.views.role.RolesView;
import pl.sgnit.ims.views.user.UsersView;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public boolean authenticate(String username, String password) {
        Optional<User> optionalUser = userService.findByUsername(username);

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

    private void createRoutes() {
        RouteConfiguration.forSessionScope().setRoute("about", AboutView.class, MainView.class);
        RouteConfiguration.forSessionScope().setRoute("logout", LogoutView.class);
        RouteConfiguration.forSessionScope().setRoute("roles", RolesView.class, MainView.class);
        RouteConfiguration.forSessionScope().setRoute("users", UsersView.class, MainView.class);
    }
}
