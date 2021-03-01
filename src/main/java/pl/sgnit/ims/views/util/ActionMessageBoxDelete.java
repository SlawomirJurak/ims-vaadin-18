package pl.sgnit.ims.views.util;

import com.vaadin.flow.component.Component;

public class ActionMessageBoxDelete extends ActionMessageBox {

    public ActionMessageBoxDelete(Component message, Action deleteAction, Action cancelAction) {
        super("Delete", "Cancel", message, deleteAction, cancelAction);
    }
}
