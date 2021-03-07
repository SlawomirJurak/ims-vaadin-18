package pl.sgnit.ims.backend.administration.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;
import pl.sgnit.ims.backend.administration.contentpanel.ContentPanelService;
import pl.sgnit.ims.backend.administration.role.Role;
import pl.sgnit.ims.views.about.AboutView;
import pl.sgnit.ims.views.calendar.weekshcedule.WeekScheduleView;
import pl.sgnit.ims.views.logout.LogoutView;
import pl.sgnit.ims.views.main.MainView;
import pl.sgnit.ims.views.administration.role.RolesView;
import pl.sgnit.ims.views.administration.user.UsersView;
import pl.sgnit.ims.views.util.ViewConfiguration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    public static final String GRANTED_VIEWS = "grantedViews";

    private final UserService userService;
    private final ContentPanelService contentPanelService;

    public AuthService(UserService userService, ContentPanelService contentPanelService) {
        this.userService = userService;
        this.contentPanelService = contentPanelService;
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
        VaadinSession.getCurrent().setAttribute("grantedViews", contentPanelService.getContentPanelsClasses());
    }

    private void createGrantedRoutes(Set<Role> grantedRoles) {
        Set<Class<? extends Component>> grantedViews = new HashSet<>();

        grantedRoles.forEach(role -> {
            role.getContentPanels().forEach(contentPanel -> {
                Class<? extends Component> contentPanelClass = contentPanelService.getContentPanelClass(contentPanel.getViewId());

                RouteConfiguration.forSessionScope().setRoute(
                    contentPanelClass.getAnnotation(ViewConfiguration.class).url(),
                    contentPanelClass,
                    MainView.class);
                grantedViews.add(contentPanelClass);
            });
        });
        VaadinSession.getCurrent().setAttribute("grantedViews", grantedViews);
    }
}
