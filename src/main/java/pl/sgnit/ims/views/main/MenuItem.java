package pl.sgnit.ims.views.main;

import com.vaadin.flow.component.Component;

public class MenuItem {

    private String title;

    private Class<? extends Component> navigationTarget;

    public MenuItem(String title, Class<? extends Component> navigationTarget) {
        this.title = title;
        this.navigationTarget = navigationTarget;
    }

    public String getTitle() {
        return title;
    }

    public Class<? extends Component> getNavigationTarget() {
        return navigationTarget;
    }
}
