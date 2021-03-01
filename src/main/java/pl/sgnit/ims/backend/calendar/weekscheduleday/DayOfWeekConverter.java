package pl.sgnit.ims.backend.calendar.weekscheduleday;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, String> {
    @Override
    public String convertToDatabaseColumn(DayOfWeek dayOfWeek) {

        if (dayOfWeek == null) {
            return null;
        }

        return switch (dayOfWeek) {
            case MONDAY -> "Monday";
            case TUESDAY -> "Tuesday";
            case WEDNESDAY -> "Wednesday";
            case THURSDAY -> "Thursday";
            case FRIDAY -> "Friday";
            case SATURDAY -> "Saturday";
            case SUNDAY -> "Sunday";
        };
    }

    @Override
    public DayOfWeek convertToEntityAttribute(String s) {

        if (s == null) {
            return null;
        }

        return switch (s) {
            case "Monday" -> DayOfWeek.MONDAY;
            case "Tuesday" -> DayOfWeek.TUESDAY;
            case "Wednesday" -> DayOfWeek.WEDNESDAY;
            case "Thursday" -> DayOfWeek.THURSDAY;
            case "Friday" -> DayOfWeek.FRIDAY;
            case "Saturday" -> DayOfWeek.SATURDAY;
            case "Sunday" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
