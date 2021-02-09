package pl.sgnit.ims.views.calendar.weekscedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import pl.sgnit.ims.backend.calendar.weekschedule.WeekSchedule;
import pl.sgnit.ims.backend.calendar.weekschedule.WeekScheduleService;
import pl.sgnit.ims.views.util.ViewConfiguration;

@PageTitle("Week Schedule")
@ViewConfiguration(id = "week schedule",
    title = "Calendar - Week Schedule",
    description = "Define week schedule which day is business or off",
    path = {"Calendar"}
)
public class WeekScheduleView extends VerticalLayout {

    private final WeekScheduleService weekScheduleService;

    private ComboBox<WeekSchedule> weekScheduleComboBox;
    private TextField weekScheduleEdit;
    private Button newWeekScheduleButton;
    private Button editWeekScheduleButton;
    private Button deleteWeekScheduleButton;
    private Button saveButton;
    private Button cancelButton;

    private Component newEdit;

    private WeekSchedule weekSchedule;

    public WeekScheduleView(WeekScheduleService weekScheduleService) {
        this.weekScheduleService = weekScheduleService;
        createLayout();
    }

    private void createLayout() {
        weekScheduleComboBox = new ComboBox<>("Week schedule");

        weekScheduleComboBox.setAllowCustomValue(false);
        weekScheduleComboBox.setWidthFull();
        weekScheduleComboBox.setItemLabelGenerator(WeekSchedule::getDescription);
        weekScheduleComboBox.addValueChangeListener(event -> weekScheduleValueChanged());
        newWeekScheduleButton = new Button("New");
        editWeekScheduleButton = new Button("Edit");
        deleteWeekScheduleButton = new Button("Delete");
        deleteWeekScheduleButton.setWidth("140px");
        weekScheduleEdit = new TextField("New or Edit");
        weekScheduleEdit.setWidthFull();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        cancelButton.setWidth("140px");

        HorizontalLayout row = new HorizontalLayout();

        row.setWidthFull();
        row.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        row.add(
            weekScheduleComboBox,
            newWeekScheduleButton,
            editWeekScheduleButton,
            deleteWeekScheduleButton
        );
        add(row);

        row = new HorizontalLayout();
        row.setWidthFull();
        row.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        newEdit = row;
        row.add(
            weekScheduleEdit,
            saveButton,
            cancelButton
        );
        row.setVisible(false);
        add(row);

        newWeekScheduleButton.addClickListener(buttonClickEvent -> newWeekSchedule());
        editWeekScheduleButton.addClickListener(buttonClickEvent -> editWeekSchedule());
        deleteWeekScheduleButton.addClickListener(buttonClickEvent -> deleteWeekSchedule());
        saveButton.addClickListener(buttonClickEvent -> saveWeekSchedule());
        cancelButton.addClickListener(buttonClickEvent -> cancelEdit());

        refreshWeekScheduleList();
    }

    private void saveWeekSchedule() {
        showEditRow(false);
        weekSchedule.setDescription(weekScheduleEdit.getValue());
        weekSchedule = weekScheduleService.save(weekSchedule);
        refreshWeekScheduleList();
        weekScheduleComboBox.setValue(weekSchedule);
    }

    private void cancelEdit() {
        showEditRow(false);
    }

    private void showEditRow(boolean visible) {
        newEdit.setVisible(visible);
        setEnableWeekScheduleRow(!visible);
    }

    private void editWeekSchedule() {
        if (weekScheduleComboBox.getValue() == null) {
            return;
        }
        weekScheduleEdit.setLabel("Edit week schedule");
        weekSchedule = weekScheduleComboBox.getValue();
        weekScheduleEdit.setValue(weekSchedule.getDescription());
        showEditRow(true);
    }

    private void newWeekSchedule() {
        weekScheduleEdit.setLabel("New week schedule");
        weekScheduleEdit.setValue("");
        weekSchedule = new WeekSchedule();
        showEditRow(true);
    }

    private void deleteWeekSchedule() {
        Notification notification = new Notification();

        Div text = new Div();
        Span scheduleName = new Span(weekScheduleComboBox.getValue().getDescription());
        scheduleName.getStyle().set("font-weight", "bold");
        text.add(
            new Text("Delete "),
            scheduleName,
            new Text(" schedule?")
        );

        Button delete = new Button("Delete");
        delete.getStyle().set("margin-left", "auto");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(buttonClickEvent -> {
            doDeleteWeekSchedule();
            notification.close();
        });

        Button cancel = new Button("Cancel");
        cancel.addClickListener(buttonClickEvent -> notification.close());

        HorizontalLayout buttons = new HorizontalLayout(delete, cancel);
        buttons.setWidthFull();
        buttons.setAlignItems(Alignment.END);
        VerticalLayout content = new VerticalLayout(text, buttons);

        notification.add(content);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.open();
    }

    private void setEnableWeekScheduleRow(boolean enabled) {
        newWeekScheduleButton.setEnabled(enabled);
        editWeekScheduleButton.setEnabled(enabled);
        weekScheduleComboBox.setEnabled(enabled);
    }

    private void refreshWeekScheduleList() {
        ComboBox.ItemFilter<WeekSchedule> filter =
            (weekScheduleItem, filterString) -> weekScheduleItem.getDescription().contains(filterString);

        weekScheduleComboBox.setItems(filter, weekScheduleService.findAll());
        weekScheduleValueChanged();
    }

    private void weekScheduleValueChanged() {
        editWeekScheduleButton.setEnabled(weekScheduleComboBox.getValue() != null);
        deleteWeekScheduleButton.setEnabled(weekScheduleComboBox.getValue() != null);
    }

    private void doDeleteWeekSchedule() {
        weekScheduleService.deleteWeekSchedule(weekScheduleComboBox.getValue());
        refreshWeekScheduleList();
    }
}
