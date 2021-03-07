package pl.sgnit.ims.views;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class IndexView extends VerticalLayout {
    public IndexView() {
        add(
            new H1("Welcome to IMS Vaadin 18"),
            new Anchor("/login", "Log into...")
        );
    }
}
