package pl.sgnit.ims.views.calendar.weekscheduleday;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.sgnit.ims.backend.calendar.weekscheduleday.DayOfWeek;
import pl.sgnit.ims.backend.calendar.weekscheduleday.DayOfWeekConverter;
import pl.sgnit.ims.backend.calendar.weekscheduleday.DayType;
import pl.sgnit.ims.backend.calendar.weekscheduleday.DayTypeConverter;
import pl.sgnit.ims.backend.calendar.weekscheduleday.WeekScheduleDay;
import pl.sgnit.ims.backend.calendar.weekscheduleday.WeekScheduleDayService;
import pl.sgnit.ims.views.util.ActionMessageBoxYesNo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeekScheduleDayView extends VerticalLayout {

    private final WeekScheduleDayService weekScheduleDayService;

    private Grid<WeekScheduleDay> weekDays;
    private DayOfWeekConverter dayOfWeekConverter = new DayOfWeekConverter();
    private Map<DayOfWeek, ComboBox<DayType>> weekDayTypes = new HashMap<>();
    private DayTypeConverter dayTypeConverter = new DayTypeConverter();
    private Button saveButton = new Button("Save");

    private Long weekScheduleId = null;
    private List<WeekScheduleDay> weekScheduleDayList;

    public WeekScheduleDayView(WeekScheduleDayService weekScheduleDayService) {
        this.weekScheduleDayService = weekScheduleDayService;
        this.weekScheduleDayList = weekScheduleDayService.getEmptyWeekSchedule();

        Stream.of(DayOfWeek.values()).forEach(dayOfWeek -> {
            ComboBox<DayType> control = new ComboBox<>();

            control.setItems(Arrays.stream(DayType.values())
                .collect(Collectors.toList())
            );
            control.setItemLabelGenerator(item -> dayTypeConverter.convertToDatabaseColumn(item));
            control.addValueChangeListener(event -> dayTypeChanged(event.getSource()));
            weekDayTypes.put(dayOfWeek, control);
        });
        setWidthFull();
        weekDays = new Grid<>(WeekScheduleDay.class);
        weekDays.setWidthFull();
        weekDays.setHeight("450px");
        weekDays.removeAllColumns();
        weekDays.addComponentColumn(item -> new Label(dayOfWeekConverter.convertToDatabaseColumn(item.getDayOfWeek())))
            .setHeader("Day of Week");
        weekDays.addComponentColumn(this::createDayTypeControl)
            .setHeader("Day type");
        weekDays.setItems(this.weekScheduleDayList);
        setDayTypeControlsReadOnly(true);
        saveButton.setEnabled(false);
        saveButton.addClickListener(buttonClickEvent -> saveWeekDaysData());
        add(weekDays, saveButton);
    }

    public void updateData(Long weekScheduleId) {
        this.weekScheduleId = weekScheduleId;
        if (weekScheduleId == null) {
            clearData();
            return;
        }

        weekScheduleDayList = weekScheduleDayService.getWeekScheduleByWeekScheduleId(weekScheduleId);
        weekScheduleDayList.forEach(weekScheduleDay -> {
            ComboBox<DayType> control = weekDayTypes.get(weekScheduleDay.getDayOfWeek());

            control.setValue(weekScheduleDay.getDayType());
            ComponentUtil.setData(control, "dayData", weekScheduleDay);
        });
        weekScheduleDayList.forEach(this::setWeekDayDayType);
        setDayTypeControlsReadOnly(false);
        saveButton.setEnabled(false);
    }

    public void clearAndDisable() {
        if (saveButton.isEnabled()) {
            Div message = new Div();

            message.add(new Text("Save changed week days schedule?"));

            ActionMessageBoxYesNo actionMessageBoxYesNo = new ActionMessageBoxYesNo(
                message,
                () -> {
                    saveWeekDaysData();
                    clearData();
                },
                null
            );
            actionMessageBoxYesNo.open();
        } else {
            clearData();
        }
    }

    private ComboBox<DayType> createDayTypeControl(WeekScheduleDay weekScheduleDay) {
        ComboBox<DayType> control = weekDayTypes.get(weekScheduleDay.getDayOfWeek());

        ComponentUtil.setData(control, "dayData", weekScheduleDay);
        control.setValue(weekScheduleDay.getDayType() == null ? control.getEmptyValue() : weekScheduleDay.getDayType());
        return control;
    }

    private void setWeekDayDayType(WeekScheduleDay weekScheduleDay) {
        weekDayTypes.get(weekScheduleDay.getDayOfWeek())
            .setValue(weekScheduleDay.getDayType());
    }

    public void setDayTypeControlsReadOnly(boolean readOnly) {
        weekDayTypes.values().forEach(control -> control.setReadOnly(readOnly));
    }

    private void clearData() {
        weekDayTypes.values().forEach(control -> control.setValue(control.getEmptyValue()));
        setDayTypeControlsReadOnly(true);
        saveButton.setEnabled(false);
    }

    private void dayTypeChanged(ComboBox<DayType> control) {
        WeekScheduleDay weekScheduleDay = (WeekScheduleDay) ComponentUtil.getData(control, "dayData");

        weekScheduleDay.setDayType(control.getValue());
        saveButton.setEnabled(true);
    }

    private void saveWeekDaysData() {
        weekScheduleDayService.saveWeekDaysData(weekScheduleDayList);
        saveButton.setEnabled(false);
    }
}
