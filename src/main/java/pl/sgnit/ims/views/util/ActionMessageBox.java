package pl.sgnit.ims.views.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

class ActionMessageBox extends Dialog {
    public ActionMessageBox(String actionButtonText, String cancelButtonText, Component message, Action action, Action cancelAction) {
        Button delete = new Button(actionButtonText);
        delete.getStyle().set("margin-left", "auto");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(buttonClickEvent -> {
            if (action != null) {
                action.doAction();
            }
            close();
        });

        Button cancel = new Button(cancelButtonText);
        cancel.addClickListener(buttonClickEvent -> {
            if (cancelAction != null) {
                cancelAction.doAction();
            }
            close();
        });

        HorizontalLayout buttons = new HorizontalLayout(delete, cancel);
        buttons.setWidthFull();
        buttons.setAlignItems(FlexComponent.Alignment.END);
        VerticalLayout content = new VerticalLayout(message, buttons);

        add(content);
    }
}
