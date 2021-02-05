package pl.sgnit.ims.views.administration.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import pl.sgnit.ims.backend.administration.role.Role;
import pl.sgnit.ims.backend.administration.user.User;

import java.util.Set;

public class UserForm extends VerticalLayout {
    private final Set<Role> roles;
    private final Binder<User> binder = new BeanValidationBinder<>(User.class);

    private final TextField username = new TextField("User name");
    private final EmailField email = new EmailField("Email");
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final Checkbox administrator = new Checkbox("Administrator");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");
    private final Button changePassword = new Button("Generate change password link");

    public UserForm(Set<Role> roles) {
        this.roles = roles;
        binder.bindInstanceFields(this);
        createLayout();
    }

    public void setUser(User user) {
        boolean newEntity;
        User loggedUser = VaadinSession.getCurrent().getAttribute(User.class);

        if (user == null) {
            newEntity = false;
        } else {
            newEntity = user.getId() == null;
        }
        binder.setBean(user);
        delete.setVisible(!newEntity);
        changePassword.setVisible(!newEntity);
        if (!newEntity) {
            if (Boolean.TRUE.equals(loggedUser.getAdministrator())) {
                delete.setEnabled(user!=null && !user.equals(loggedUser));
                administrator.setEnabled(delete.isEnabled());
            } else {
                boolean canEdit = user!=null && !user.getAdministrator();

                firstName.setEnabled(canEdit);
                lastName.setEnabled(canEdit);
                email.setEnabled(canEdit);
                username.setEnabled(canEdit);
                save.setEnabled(canEdit);
                delete.setEnabled(canEdit);
                administrator.setEnabled(false);
            }
        }
    }

    private void createLayout() {
        configureControls();
        add(
            new HorizontalLayout(username, email),
            new HorizontalLayout(firstName, lastName),
            administrator,
            createButtonsLayout()
        );
    }

    private void configureControls() {
        User loggedUser = VaadinSession.getCurrent().getAttribute(User.class);

        administrator.setVisible(loggedUser.getAdministrator());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new UserForm.DeleteEvent(this, binder.getBean())));
        cancel.addClickListener(click -> fireEvent(new UserForm.CancelEvent(this)));
        changePassword.addClickListener(click -> fireEvent(new UserForm.GenerateChangePasswordLinkEvent(this, binder.getBean())));

        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));

        return new VerticalLayout(
            new HorizontalLayout(save, delete, cancel),
            changePassword
        );
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new UserForm.SaveEvent(this, binder.getBean()));
        }
    }

    public abstract static class UserFormEvent extends ComponentEvent<UserForm> {
        private final User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, User user) {
            super(source, user);
        }

    }

    public static class CancelEvent extends UserFormEvent {
        CancelEvent(UserForm source) {
            super(source, null);
        }
    }

    public static class GenerateChangePasswordLinkEvent extends UserFormEvent {
        GenerateChangePasswordLinkEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
