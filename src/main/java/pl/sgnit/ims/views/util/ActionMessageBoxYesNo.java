package pl.sgnit.ims.views.util;

import com.vaadin.flow.component.Component;

public class ActionMessageBoxYesNo extends ActionMessageBox {
    public ActionMessageBoxYesNo(Component message, Action saveAction, Action cancelAction) {
        super("Yes", "No", message, saveAction, cancelAction);
    }
}
