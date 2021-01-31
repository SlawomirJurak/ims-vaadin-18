package pl.sgnit.ims.views.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import pl.sgnit.ims.views.about.AboutView;
import pl.sgnit.ims.views.logout.LogoutView;
import pl.sgnit.ims.views.role.RolesView;
import pl.sgnit.ims.views.user.UsersView;

import java.util.Optional;

/**
 * The main contentpanel is a top-level placeholder for other views.
 */
@CssImport("./styles/views/main/main-view.css")
@JsModule("./styles/shared-styles.js")
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        RouterLink logout = new RouterLink("Logout", LogoutView.class);
        logout.getStyle().set("marginRight", "10px");
        HorizontalLayout hl = new HorizontalLayout(
            logout,
            new Image("images/user.svg", "Avatar"));
        hl.getStyle().set("marginLeft", "auto");
        layout.add(hl);
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "IMS Vaadin 18 logo"));
        logoLayout.add(new H1("IMS Vaadin 18"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        return new Tab[]{
            createLinkTab("About", AboutView.class),
            createLinkTab("Roles", RolesView.class),
            createLinkTab("Users", UsersView.class)};
    }

    private Tab createLinkTab(String text, Class<? extends Component> navigationTarget) {
        Tab tab = new Tab();

        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    private Tab createSectionTab(String text, int indexChildTab) {
        Tab tab = new Tab();
        Paragraph tabText = new Paragraph(text);

        tabText.getStyle().set("color", "gray");
        tabText.getStyle().set("font-weight", "bold");
        tabText.addClickListener(paragraphClickEvent -> {
            if (paragraphClickEvent.getClickCount()==2) {
                menu.getComponentAt(indexChildTab).setVisible(!menu.getComponentAt(indexChildTab).isVisible());
            }
        });
        tab.add(tabText);
        ComponentUtil.setData(tab, Class.class, String.class);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        Optional<Tab> tab =  getTabForComponent(getContent());

        if (tab.isPresent()) {
            menu.setSelectedTab(tab.get());
            viewTitle.setText(getCurrentPageTitle());
        }
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
            .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
