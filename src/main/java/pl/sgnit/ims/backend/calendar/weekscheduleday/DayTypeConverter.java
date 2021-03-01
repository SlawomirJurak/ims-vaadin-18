package pl.sgnit.ims.backend.calendar.weekscheduleday;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DayTypeConverter implements AttributeConverter<DayType, String> {
    @Override
    public String convertToDatabaseColumn(DayType dayType) {

        if (dayType == null) {
            return null;
        }

        return switch (dayType) {
            case OFF -> "Off";
            case SHIFT_1 -> "1 Shift";
            case SHIFT_2 -> "2 Shifts";
            case SHIFT_3 -> "3 Shifts";
        };
    }

    @Override
    public DayType convertToEntityAttribute(String s) {

        if (s == null) {
            return null;
        }

        return switch (s) {
            case "Off" -> DayType.OFF;
            case "1 Shift" -> DayType.SHIFT_1;
            case "2 Shifts" -> DayType.SHIFT_2;
            case "3 Shifts" -> DayType.SHIFT_3;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
