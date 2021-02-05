package pl.sgnit.ims.views.login;

import com.vaadin.flow.router.Route;
import pl.sgnit.ims.backend.administration.user.AuthService;

@Route("changePassword")
public class ChangePasswordView extends DefinePasswordView {
    public ChangePasswordView(AuthService authService) {
        super(
            authService,
            "Change password",
            "Please define new password",
            "Change password"
        );
    }
}
