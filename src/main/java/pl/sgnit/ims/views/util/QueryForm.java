package pl.sgnit.ims.views.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.sgnit.ims.backend.EntityTemplate;
import pl.sgnit.ims.backend.utils.QueryConditionCreator;
import pl.sgnit.ims.backend.utils.QueryableField;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QueryForm extends VerticalLayout {

    private final FormLayout fieldsForm = new FormLayout();

    private final Map<Field, HasValue<HasValue.ValueChangeEvent<String>, String>> fieldsMap = new HashMap<>();

    private final QueryableView queryable;

    public QueryForm(QueryableView queryable, Class<? extends EntityTemplate> entityClass) {
        this.queryable = queryable;
        createComponents(entityClass);
        createButtons();
    }

    private void createComponents(Class<? extends EntityTemplate> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();

        Arrays.stream(fields)
            .filter(field -> field.isAnnotationPresent(QueryableField.class))
            .forEach(this::createFieldControl);
        add(fieldsForm);
    }

    private void createFieldControl(Field field) {
        Component control = null;

        switch (field.getType().getName()) {
            case "java.lang.String":
                control = new TextField();
                break;
            case "java.lang.Boolean":
                ComboBox<String> comboBox = new ComboBox<>();

                comboBox.setItems("FALSE", "TRUE");
                control = comboBox;
                break;
        }
        if (control != null) {
            fieldsForm.addFormItem(control, field.getName());
            fieldsMap.put(field, (HasValue<HasValue.ValueChangeEvent<String>, String>) control);
        }
    }

    private void createButtons() {
        Button doQuery = new Button("Do query", buttonClickEvent -> doUserQuery());
        Button countRecords = new Button("Count records", buttonClickEvent -> countRecords());
        Button reset = new Button("Reset", buttonClickEvent -> resetFields());
        Button cancel = new Button("Cancel", buttonClickEvent -> setVisible(false));

        HorizontalLayout actions = new HorizontalLayout();
        actions.add(
            doQuery,
            countRecords,
            reset,
            cancel
        );
        doQuery.getStyle().set("marginRight", "10px");
        countRecords.getStyle().set("marginRight", "10px");
        reset.getStyle().set("marginRight", "10px");
        add(actions);
    }

    private void countRecords() {
        long recordCount = queryable.countRecords(prepareWhereCondition());
        Dialog dialog = new Dialog();

        dialog.add(new VerticalLayout(
            new Text("Found "+recordCount+" records"),
            new Button("Close", buttonClickEvent -> dialog.close())
        ));
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.open();
    }

    private void doUserQuery() {
        queryable.doUserQuery(prepareWhereCondition());
        setVisible(false);
    }

    private void resetFields() {
        fieldsMap.values()
            .forEach(HasValue::clear);
    }

    private Map<Field, String> prepareConditions() {
        Map<Field, String> conditions = new HashMap<>();

        fieldsMap.entrySet().stream()
            .forEach(entry -> {
                String condition = entry.getValue().getValue();

                if (condition != null && !condition.isEmpty()) {
                    conditions.put(entry.getKey(), condition);
                }
            });
        return conditions;
    }

    private String prepareWhereCondition() {
        Map<Field, String> conditions = prepareConditions();

        return QueryConditionCreator.prepareQueryCondition(conditions, queryable.getFieldPrefix());
    }
}
