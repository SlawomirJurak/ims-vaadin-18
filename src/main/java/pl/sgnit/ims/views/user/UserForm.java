package pl.sgnit.ims.views.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import pl.sgnit.ims.backend.role.Role;
import pl.sgnit.ims.backend.user.User;

import java.util.Set;

public class UserForm extends VerticalLayout {
    private final Set<Role> roles;
    private final Binder<User> binder = new BeanValidationBinder<>(User.class);

    private final TextField username = new TextField("User name");
    private final EmailField email = new EmailField("Email");
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");

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

        if (user == null) {
            newEntity = false;
        } else {
            newEntity = user.getId() == null;
        }
        binder.setBean(user);
        delete.setVisible(!newEntity);
        changePassword.setVisible(!newEntity);
    }

    private void createLayout() {
        add(
            new HorizontalLayout(username, email),
            new HorizontalLayout(firstName, lastName),
            createButtonsLayout()
        );
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

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
