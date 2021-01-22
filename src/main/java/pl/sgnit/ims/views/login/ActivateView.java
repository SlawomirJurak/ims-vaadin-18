package pl.sgnit.ims.views.login;

import com.vaadin.flow.router.Route;
import pl.sgnit.ims.backend.user.AuthService;

@Route(value = "activate")
public class ActivateView extends DefinePasswordView {

    public ActivateView(AuthService authService) {
        super(
            authService,
            "Activating account",
            "Please define password",
            "Activate"
        );
    }
}
