package pl.sgnit.ims.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import pl.sgnit.ims.backend.role.RoleService;
import pl.sgnit.ims.backend.user.User;
import pl.sgnit.ims.backend.user.UserService;
import pl.sgnit.ims.views.util.QueryForm;
import pl.sgnit.ims.views.util.QueryableView;
import pl.sgnit.ims.views.util.ViewPath;

import java.util.Set;

@PageTitle("Users")
@ViewPath("users")
public class UsersView extends VerticalLayout implements QueryableView {

    private final UserService userService;
    private final RoleService roleService;

    private Grid<User> userGrid = new Grid<>(User.class);
    private QueryForm queryForm;
    private UserForm userForm;
    private Button showQueryForm;
    private Button showAllRecords;
    private Button newUser;

    public UsersView(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        queryForm = new QueryForm(this, User.class);
        queryForm.setVisible(false);
        showQueryForm = new Button(
            "Query",
            buttonClickEvent -> showQueryForm()
        );
        showAllRecords = new Button(
            "All users",
            buttonClickEvent -> updateList()
        );
        newUser = new Button(
            "New user",
            buttonClickEvent -> addNewUser()
        );
        userForm = new UserForm(Set.copyOf(roleService.findAll("")));
        userForm.setVisible(false);
        userForm.addListener(UserForm.SaveEvent.class, this::saveUser);
        userForm.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        userForm.addListener(UserForm.CancelEvent.class, cancelEvent -> closeEditor());
        userForm.addListener(UserForm.GenerateChangePasswordLinkEvent.class, this::generateChangePasswordLink);

        setSizeFull();
        configureGrid();
        HorizontalLayout dataLayout = new HorizontalLayout();
        dataLayout.setSizeFull();
        dataLayout.add(
            userGrid,
            queryForm,
            userForm
        );
        add(
            new HorizontalLayout(showAllRecords, showQueryForm, newUser),
            dataLayout
        );
        updateList();
    }

    private void configureGrid() {
        userGrid.setSizeFull();
        userGrid.setColumns("username", "firstName", "lastName", "email");
        userGrid.addComponentColumn(item -> {
            Checkbox checkbox = new Checkbox();
            Boolean administrator = item.getAdministrator();

            administrator = administrator != null && administrator;
            checkbox.setValue(administrator);
            checkbox.setReadOnly(true);
            return checkbox;
        }).setHeader("Administrator")
            .setKey("administrator")
            .setTextAlign(ColumnTextAlign.CENTER);
        userGrid.getColumnByKey("username").setHeader("Login");
        userGrid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue()));
    }

    private void updateList() {
        userGrid.setItems(userService.findAll());
    }

    private void showQueryForm() {
        userForm.setVisible(false);
        queryForm.setVisible(true);
    }

    private void editUser(User user) {
        if (user == null) {
            closeEditor();
            return;
        }
        userForm.setUser(user);
        showEditor();
    }

    private void closeEditor() {
        userForm.setUser(null);
        userForm.setVisible(false);
    }

    private void showEditor() {
        queryForm.setVisible(false);
        userForm.setVisible(true);
    }

    private void saveUser(UserForm.SaveEvent event) {
        userService.save(event.getUser());
        updateList();
        closeEditor();
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        userService.delete(event.getUser());
        updateList();
        closeEditor();
    }

    private void generateChangePasswordLink(UserForm.GenerateChangePasswordLinkEvent event) {
        userService.generateChangePasswordLink(event.getUser());
    }

    private void addNewUser() {
        editUser(new User());
    }

    @Override
    public void doUserQuery(String where) {
        userGrid.setItems(userService.doUserQuery(where));
    }

    @Override
    public long countRecords(String where) {
        return userService.count(where);
    }

    @Override
    public String getFieldPrefix() {
        return "u";
    }
}
