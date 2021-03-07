package pl.sgnit.ims.views.calendar.weekshcedule;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import pl.sgnit.ims.backend.calendar.weekschedule.WeekSchedule;
import pl.sgnit.ims.backend.calendar.weekschedule.WeekScheduleService;
import pl.sgnit.ims.backend.calendar.weekscheduleday.WeekScheduleDayService;
import pl.sgnit.ims.views.calendar.weekscheduleday.WeekScheduleDayView;
import pl.sgnit.ims.views.util.ActionMessageBoxDelete;
import pl.sgnit.ims.views.util.ViewConfiguration;

@PageTitle("Week Schedule")
@ViewConfiguration(id = "week schedule",
    title = "Calendar - Week Schedule",
    description = "Define week schedule which day is business or off",
    path = {"Calendar"},
    url = "weekSchedule"
)
public class WeekScheduleView extends VerticalLayout {

    private final WeekScheduleService weekScheduleService;
    private final WeekScheduleDayService weekScheduleDayService;

    private ComboBox<WeekSchedule> weekScheduleComboBox;
    private TextField weekScheduleEdit;
    private Button newWeekScheduleButton;
    private Button editWeekScheduleButton;
    private Button deleteWeekScheduleButton;
    private Button saveButton;
    private Button cancelButton;
    private WeekScheduleDayView weekScheduleDayView;

    private boolean edited;

    private Component newEdit;

    public WeekScheduleView(WeekScheduleService weekScheduleService, WeekScheduleDayService weekScheduleDayService) {
        this.weekScheduleService = weekScheduleService;
        this.weekScheduleDayService = weekScheduleDayService;
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
        weekScheduleDayView = new WeekScheduleDayView(weekScheduleDayService);
        add(weekScheduleDayView);

        newWeekScheduleButton.addClickListener(buttonClickEvent -> newWeekSchedule());
        editWeekScheduleButton.addClickListener(buttonClickEvent -> editWeekSchedule());
        deleteWeekScheduleButton.addClickListener(buttonClickEvent -> deleteWeekSchedule());
        saveButton.addClickListener(buttonClickEvent -> saveWeekSchedule());
        cancelButton.addClickListener(buttonClickEvent -> cancelEdit());

        refreshWeekScheduleList();
    }

    private void saveWeekSchedule() {
        WeekSchedule weekSchedule;

        showEditRow(false);

        if (edited) {
            weekSchedule = weekScheduleComboBox.getValue();
        } else {
            weekSchedule = new WeekSchedule();
        }
        weekSchedule.setDescription(weekScheduleEdit.getValue());
        weekSchedule = weekScheduleService.save(weekSchedule);
        refreshWeekScheduleList();
        weekScheduleComboBox.setValue(weekSchedule);
        weekScheduleDayView.setDayTypeControlsReadOnly(false);
    }

    private void cancelEdit() {
        showEditRow(false);
        weekScheduleDayView.updateData(weekScheduleComboBox.getValue().getId());
        weekScheduleDayView.setDayTypeControlsReadOnly(false);
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
        WeekSchedule weekSchedule = weekScheduleComboBox.getValue();
        weekScheduleEdit.setValue(weekSchedule.getDescription());
        showEditRow(true);
        weekScheduleDayView.setDayTypeControlsReadOnly(true);
        edited = true;
    }

    private void newWeekSchedule() {
        weekScheduleDayView.clearAndDisable();
        weekScheduleEdit.setLabel("New week schedule");
        weekScheduleEdit.setValue("");
        WeekSchedule weekSchedule = new WeekSchedule();
        showEditRow(true);
        edited = false;
    }

    private void deleteWeekSchedule() {
        if (weekScheduleComboBox.getValue() == null) {
            return;
        }

        Div text = new Div();
        Span scheduleName = new Span(weekScheduleComboBox.getValue().getDescription());
        scheduleName.getStyle().set("font-weight", "bold");
        text.add(
            new Text("Delete "),
            scheduleName,
            new Text(" schedule?")
        );

        ActionMessageBoxDelete messageBoxDelete = new ActionMessageBoxDelete(
            text,
            () -> doDeleteWeekSchedule(),
            null);

        messageBoxDelete.open();
    }

    private void setEnableWeekScheduleRow(boolean enabled) {
        newWeekScheduleButton.setEnabled(enabled);
        editWeekScheduleButton.setEnabled(enabled);
        deleteWeekScheduleButton.setEnabled(enabled);
        weekScheduleComboBox.setEnabled(enabled);
    }

    private void refreshWeekScheduleList() {
        weekScheduleComboBox.setItems(weekScheduleService.findAll());
    }

    private void weekScheduleValueChanged() {
        WeekSchedule weekSchedule = weekScheduleComboBox.getValue();

        editWeekScheduleButton.setEnabled(weekSchedule != null);
        deleteWeekScheduleButton.setEnabled(weekSchedule != null);
        weekScheduleDayView.updateData(
            weekSchedule == null ? null : weekSchedule.getId()
        );
    }

    private void doDeleteWeekSchedule() {
        weekScheduleService.deleteWeekSchedule(weekScheduleComboBox.getValue());
        refreshWeekScheduleList();
    }
}
