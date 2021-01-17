package pl.sgnit.ims.backend.utils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryConditionCreator {

    private QueryConditionCreator() {
    }

    public static String prepareQueryCondition(Map<Field, String> conditions, String fieldPrefix) {
        List<String> conditionList = new ArrayList<>();

        conditions.forEach((key, value) -> conditionList.add(prepareFieldCondition(key, value, fieldPrefix)));

        return String.join(" and ", conditionList);
    }

    private static String prepareFieldCondition(Field field, String conditionData, String fieldPrefix) {
        if (conditionData.contains(";")) {
            return prepareOrCondition(field, conditionData, fieldPrefix);
        }
        return prepareCondition(field, conditionData, fieldPrefix);
    }

    private static String prepareOrCondition(Field field, String conditionData, String fieldPrefix) {
        String[] conditions = conditionData.split(";");
        String orCondition = Arrays.stream(conditions).map(condition -> prepareCondition(field, condition, fieldPrefix))
            .collect(Collectors.joining(" or "));
        return "(" + orCondition + ")";
    }

    private static String prepareCondition(Field field, String conditionData, String fieldPrefix) {
        if (conditionData.contains("..")) {
            return prepareBetweenCondition(field, conditionData, fieldPrefix);
        }
        if (conditionData.equals("!")) {
            return prepareIsNullCondition(field, fieldPrefix);
        }
        if (conditionData.equals("!!")) {
            return prepareIsNotNullCondition(field, fieldPrefix);
        }
        if (conditionData.contains("%") || conditionData.contains("_")) {
            return prepareLikeCondition(field, conditionData, fieldPrefix);
        }
        return prepareCommonCondition(field, conditionData, fieldPrefix);
    }

    private static String prepareBetweenCondition(Field field, String conditionData, String fieldPrefix) {
        String[] values = conditionData.split("\\.\\.");

        if (values.length != 2) {
            throw new IncorrectConditionDataException(field, conditionData);
        }

        String fieldName = getColumnName(field, fieldPrefix);

        return fieldName +
            " between " +
            formatConditionData(field, values[0]) +
            " and " +
            formatConditionData(field, values[1]);
    }

    private static String prepareIsNullCondition(Field field, String fieldPrefix) {
        return getColumnName(field, fieldPrefix) + " is null";
    }

    private static String prepareIsNotNullCondition(Field field, String fieldPrefix) {
        return getColumnName(field, fieldPrefix) + " is not null";
    }

    private static String prepareLikeCondition(Field field, String conditionData, String fieldPrefix) {
        return getColumnToChar(field, fieldPrefix) + " like '" + conditionData + "'";
    }

    private static String getColumnToChar(Field field, String fieldPrefix) {
        String fieldType = field.getType().getName();
        String column = field.getAnnotation(Column.class).name();

        return switch (fieldType) {
            case "java.lang.String" -> getColumnName(field, fieldPrefix);
            case "java.lang.Integer" -> "to_char(u."+column+", 'FM9999999999')";
            case "java.lang.Long" -> "to_char(u."+column+", 'FM9999999999999999999')";
            case "java.math.BigDecimal" -> "to_char(u."+column+", 'FM999999999999999.99999999999999999')";
            case "java.time.LocalDate" -> "to_char(u."+column+", 'YYYY-MM-DD')";
            default -> throw new IncorrectConditionDataException(field, "Unknown type of data " + fieldType);
        };

    }

    private static String prepareCommonCondition(Field field, String conditionData, String fieldPrefix) {
        if (conditionData.startsWith(">=") || conditionData.startsWith("<=") || conditionData.startsWith("<>")) {
            return getColumnName(field, fieldPrefix) + " " + conditionData.substring(0, 2) + " " + formatConditionData(field, conditionData.substring(2));
        }
        if (conditionData.startsWith(">") || conditionData.startsWith("<")) {
            return getColumnName(field, fieldPrefix) + " " + conditionData.charAt(0) + " " + formatConditionData(field, conditionData.substring(1));
        }
        return getColumnName(field, fieldPrefix) + " = " + formatConditionData(field, conditionData);
    }

    private static String formatConditionData(Field field, String conditionData) {
        String fieldType = field.getType().getName();

        return switch (fieldType) {
            case "java.lang.String" -> formatString(conditionData);
            case "java.lang.Integer" -> formatInteger(field, conditionData);
            case "java.lang.Long" -> formatLong(field, conditionData);
            case "java.math.BigDecimal" -> formatBigDecimal(field, conditionData);
            case "java.time.LocalDate" -> formatDate(field, conditionData);
            case "java.lang.Boolean" -> formatBoolean(field, conditionData);
            default -> throw new IncorrectConditionDataException(field, "Unknown type of data " + fieldType);
        };
    }

    private static String formatBoolean(Field field, String conditionData) {
        if (!("true".equals(conditionData) || "false".equals(conditionData))) {
            throw new IncorrectConditionDataException(field, conditionData);
        }
        return conditionData;
    }

    private static String formatLong(Field field, String conditionData) {
        try {
            Long.parseLong(conditionData);
        } catch (NumberFormatException ex) {
            throw new IncorrectConditionDataException(field, conditionData);
        }
        return conditionData;
    }

    private static String formatInteger(Field field, String conditionData) {
        try {
            Integer.parseInt(conditionData);
        } catch (NumberFormatException ex) {
            throw new IncorrectConditionDataException(field, conditionData);
        }
        return conditionData;
    }

    private static String formatBigDecimal(Field field, String conditionData) {
        try {
            new BigDecimal(conditionData);
        } catch (NumberFormatException ex) {
            throw new IncorrectConditionDataException(field, conditionData);
        }
        return conditionData;
    }

    private static String formatDate(Field field, String conditionData) {
        try {
            LocalDate.parse(conditionData);
        } catch (DateTimeParseException ex) {
            throw new IncorrectConditionDataException(field, conditionData);
        }
        return "'" + conditionData + "'";
    }

    private static String formatString(String value) {
        return "'" + value + "'";
    }

    private static String getColumnName(Field field, String fieldPrefix) {
        Column column = field.getAnnotation(Column.class);

        return fieldPrefix + "." + column.name();
    }
}
