package pl.sgnit.ims.views.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import pl.sgnit.ims.backend.administration.user.AuthService;
import pl.sgnit.ims.views.about.AboutView;
import pl.sgnit.ims.views.administration.role.RolesView;
import pl.sgnit.ims.views.administration.user.UsersView;
import pl.sgnit.ims.views.calendar.weekshcedule.WeekScheduleView;
import pl.sgnit.ims.views.logout.LogoutView;

import java.util.Set;

@CssImport("./styles/views/main/main-view.css")
@CssImport(value = "./styles/views/main/app-layout.css", themeFor = "vaadin-app-layout")
@JsModule("./styles/shared-styles.js")
public class MainView extends AppLayout {

    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent(createTreeMenu()));
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

    private Component createDrawerContent(Component menu) {
        VerticalLayout layout = new VerticalLayout();

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.add(createLogo(), menu);
        return layout;
    }

    private Component createLogo() {
        HorizontalLayout logoLayout = new HorizontalLayout();

        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "IMS Vaadin 18 logo"));
        logoLayout.add(new H1("IMS Vaadin 18"));
        return logoLayout;
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

    private Component createSectionTab(String text) {
        Div tabText = new Div();

        tabText.setText(text);
        tabText.getStyle().set("color", "gray");
        tabText.getStyle().set("font-weight", "bold");
        ComponentUtil.setData(tabText, Class.class, String.class);
        return tabText;
    }

    private Component createTreeMenu() {
        TreeData<MenuItem> menuItemTreeData = new TreeData<>();
        TreeData<MenuItem> item;
        MenuItem rootMenuItem;

        rootMenuItem = new MenuItem("About", AboutView.class);
        menuItemTreeData.addRootItems(rootMenuItem);
        createAdministrationMenu(menuItemTreeData);
        createCalendarMenu(menuItemTreeData);

        TreeDataProvider<MenuItem> treeDataProvider = new TreeDataProvider<>(menuItemTreeData);
        TreeGrid<MenuItem> menuTree = new TreeGrid<>();
        menuTree.setDataProvider(treeDataProvider);
        menuTree.addComponentHierarchyColumn(treeItem -> {
            if (treeItem.getNavigationTarget() == null) {
                return createSectionTab(treeItem.getTitle());
            }
            return createLinkTab(treeItem.getTitle(), treeItem.getNavigationTarget());
        });
        return menuTree;
    }

    private void createAdministrationMenu(TreeData<MenuItem> menu) {
        Set<Class<? extends Component>> grantedViews = getGrantedViews();

        if (grantedViews.contains(RolesView.class) || grantedViews.contains(UsersView.class)) {
            TreeData<MenuItem> treeItem;
            MenuItem menuItem;

            menuItem = new MenuItem("Administration", null);
            treeItem = menu.addRootItems(menuItem);
            if (grantedViews.contains(RolesView.class)) {
                treeItem.addItem(menuItem, new MenuItem("Roles", RolesView.class));
            }
            if (grantedViews.contains(UsersView.class)) {
                treeItem.addItem(menuItem, new MenuItem("Users", UsersView.class));
            }

        }
    }

    private void createCalendarMenu(TreeData<MenuItem> menu) {
        Set<Class<? extends Component>> grantedViews = getGrantedViews();

        if (grantedViews.contains(WeekScheduleView.class)) {
            TreeData<MenuItem> treeItem;
            MenuItem menuItem;

            menuItem = new MenuItem("Calendar", null);
            treeItem = menu.addRootItems(menuItem);
            if (grantedViews.contains(WeekScheduleView.class)) {
                treeItem.addItem(menuItem, new MenuItem("Week schedule", WeekScheduleView.class));
            }
        }
    }

    private Set<Class<? extends Component>> getGrantedViews() {
        return (Set<Class<? extends Component>>) VaadinSession.getCurrent().getAttribute(AuthService.GRANTED_VIEWS);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
