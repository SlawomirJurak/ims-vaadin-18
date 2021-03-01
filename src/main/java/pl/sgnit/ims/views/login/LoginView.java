package pl.sgnit.ims.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.sgnit.ims.backend.administration.user.AuthService;

@Route(value = "login")
@PageTitle("Login")
@CssImport("./styles/views/login/login-view.css")
public class LoginView extends Div {

    private final AuthService authService;

    private TextField user;
    private PasswordField password;

    public LoginView(AuthService authService) {
        this.authService = authService;

        setId("login-view");
        user = new TextField("Username");
        user.setAutofocus(true);
        password = new PasswordField("Password");
        add(
            new H1("Welcome IMS Vaadin 18"),
            user,
            password,
            new Button("Login", event -> authenticateUser())
        );
    }

    private void authenticateUser() {
        if (authService.authenticate(user.getValue(), password.getValue())) {
            UI.getCurrent().navigate("about");
            return;
        }
        Notification.show("Wrong credentials", 1500, Notification.Position.TOP_CENTER);
    }
}
