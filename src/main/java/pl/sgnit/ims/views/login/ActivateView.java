package pl.sgnit.ims.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import pl.sgnit.ims.backend.user.AuthService;

import java.util.List;
import java.util.Map;

@Route(value = "activate")
public class ActivateView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthService authService;

    private String activationCode;

    private PasswordField password1 = new PasswordField("Password");
    private PasswordField password2 = new PasswordField("Retype password");

    public ActivateView(AuthService authService) {
        this.authService = authService;

        add(
            new H1("Activating account"),
            new H4("Please define password"),
            password1,
            password2,
            new Button(
                "Activate",
                buttonClickEvent -> activate()
            )
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Map<String, List<String>> params = beforeEnterEvent.getLocation().getQueryParameters().getParameters();

        activationCode = params.get("code").get(0);
    }

    private void activate() {
        if (password1.getValue().isEmpty()) {
            Notification.show("Enter password");
            return;
        }
        if (password2.getValue().isEmpty()) {
            Notification.show("Retype password");
            return;
        }
        if (!password1.getValue().equals(password2.getValue())) {
            Notification.show("Passwords don't match");
            return;
        }
        authService.activate(activationCode, password1.getValue());
        UI.getCurrent().navigate("login");
    }

}
