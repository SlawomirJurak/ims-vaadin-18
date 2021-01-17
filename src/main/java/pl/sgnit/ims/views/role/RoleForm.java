package pl.sgnit.ims.views.role;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import pl.sgnit.ims.backend.contentpanel.ContentPanel;
import pl.sgnit.ims.backend.role.Role;

import java.util.Set;

public class RoleForm extends VerticalLayout {

    private Set<ContentPanel> contentPanelList;

    private TextField name = new TextField("Name");
    private TextField description = new TextField("Description");
    private MultiSelectListBox<ContentPanel> contentPanels = new MultiSelectListBox<>();

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button cancel = new Button("Cancel");

    private Button selectAll = new Button("Select all");
    private Button selectNone = new Button("Select none");

    private Binder<Role> binder = new BeanValidationBinder<>(Role.class);

    public RoleForm(Set<ContentPanel> contentPanelList) {
        this.contentPanelList = contentPanelList;

        binder.bindInstanceFields(this);

        add(
            createLayout()
        );
    }

    public void setRole(Role role, boolean newRole) {
        binder.setBean(role);
        delete.setVisible(!newRole);
    }

    private Component createLayout() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout row = new HorizontalLayout();

        row.add(
            name,
            description
        );
        contentPanels.setWidthFull();
        contentPanels.setItems(contentPanelList);
        layout.add(
            row,
            createContentPanelsControl(),
            createButtonsLayout()
        );
        return layout;
    }

    private Component createContentPanelsControl() {
        VerticalLayout control = new VerticalLayout();
        HorizontalLayout row = new HorizontalLayout();
        VerticalLayout buttons = new VerticalLayout();

        selectAll.addClickListener(buttonClickEvent -> {
            contentPanels.setValue(contentPanelList);
        });
        selectNone.addClickListener(buttonClickEvent -> contentPanels.clear());
        buttons.add(
            selectAll,
            selectNone
        );
        contentPanels.setRenderer(new ComponentRenderer<>(item -> {
            Label label = new Label(item.getName());
            return label;
        }));
        row.add(
            contentPanels,
            buttons
        );
        control.add(
            new Label("Panels"),
            row
        );
        return control;
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        cancel.addClickListener(click -> fireEvent(new CancelEvent(this)));

        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public static abstract class ContactFormEvent extends ComponentEvent<RoleForm> {
        private Role role;

        protected ContactFormEvent(RoleForm source, Role role) {
            super(source, false);
            this.role = role;
        }

        public Role getRole() {
            return role;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(RoleForm source, Role role) {
            super(source, role);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(RoleForm source, Role role) {
            super(source, role);
        }

    }

    public static class CancelEvent extends ContactFormEvent {
        CancelEvent(RoleForm source) {
            super(source, null);
        }
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
