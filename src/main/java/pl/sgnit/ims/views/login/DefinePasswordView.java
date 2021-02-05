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
import pl.sgnit.ims.backend.administration.user.AuthService;

import java.util.List;
import java.util.Map;

class DefinePasswordView extends VerticalLayout  implements BeforeEnterObserver {
    private final AuthService authService;

    private String code;

    private PasswordField password1 = new PasswordField("Password");
    private PasswordField password2 = new PasswordField("Retype password");

    DefinePasswordView(AuthService authService, String title, String message, String buttonCaption) {
        this.authService = authService;

        add(
            new H1(title),
            new H4(message),
            password1,
            password2,
            new Button(
                buttonCaption,
                buttonClickEvent -> setPassword()
            )
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Map<String, List<String>> params = beforeEnterEvent.getLocation().getQueryParameters().getParameters();

        code = params.get("code").get(0);
    }

    private void setPassword() {
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
        authService.setPassword(code, password1.getValue());
        UI.getCurrent().navigate("login");
    }
}
