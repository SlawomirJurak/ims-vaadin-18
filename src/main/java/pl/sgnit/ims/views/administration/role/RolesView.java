package pl.sgnit.ims.views.administration.role;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import pl.sgnit.ims.backend.administration.contentpanel.ContentPanelService;
import pl.sgnit.ims.backend.administration.role.Role;
import pl.sgnit.ims.backend.administration.role.RoleService;
import pl.sgnit.ims.views.util.ViewConfiguration;

@PageTitle("Roles")
@ViewConfiguration(id = "roles", title = "Administration - Roles", path = {"Administration"}, description = "Management of roles")
public class RolesView extends VerticalLayout {

    private final RoleService roleService;
    private final ContentPanelService contentPanelService;

    private TextField filterByName = new TextField();
    private Grid<Role> roleGrid = new Grid<>(Role.class);
    private RoleForm roleForm;

    public RolesView(RoleService roleService, ContentPanelService contentPanelService) {
        this.roleService = roleService;
        this.contentPanelService = contentPanelService;

        roleForm = new RoleForm(contentPanelService.findAll());
        setSizeFull();
        configureGrid();

        add(
            createToolbar(),
            createContent()
        );
        updateList();
        closeEditor();
    }

    private Component createToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        filterByName.setPlaceholder("Filter by name...");
        filterByName.setClearButtonVisible(true);
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByName.addValueChangeListener(event -> updateList());

        Button addRoleButton = new Button("Add Role");
        addRoleButton.addClickListener(event -> addRole());

        toolbar.add(
            filterByName,
            addRoleButton
        );
        return toolbar;
    }

    private Component createContent() {
        configureGrid();
        roleForm.addListener(RoleForm.SaveEvent.class, this::saveRole);
        roleForm.addListener(RoleForm.DeleteEvent.class, this::deleteRole);
        roleForm.addListener(RoleForm.CancelEvent.class, cancelEvent -> closeEditor());

        Div content = new Div(roleGrid, roleForm);

        content.getStyle().set("display", "flex");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        roleGrid.setSizeFull();
        roleGrid.setColumns("name", "description");
        roleGrid.asSingleSelect().addValueChangeListener(event -> editRole(event.getValue(), false));
    }

    private void addRole() {
        roleGrid.asSingleSelect().clear();
        editRole(new Role(), true);
    }

    private void updateList() {
        roleGrid.setItems(roleService.findAll(filterByName.getValue()));
    }

    private void editRole(Role role, boolean newRole) {
        if (role == null) {
            closeEditor();
        } else {
            roleForm.setRole(role, newRole);
            roleForm.setVisible(true);
        }
    }

    private void closeEditor() {
        roleForm.setRole(null, false);
        roleForm.setVisible(false);
    }

    private void deleteRole(RoleForm.DeleteEvent event) {
        roleService.delete(event.getRole());
        updateList();
        closeEditor();
    }

    private void saveRole(RoleForm.SaveEvent event) {
        roleService.save(event.getRole());
        updateList();
        closeEditor();
    }

    private void filterRolesByName() {
        roleGrid.setItems(roleService.filterAllByName(filterByName.getValue()));
    }
}
