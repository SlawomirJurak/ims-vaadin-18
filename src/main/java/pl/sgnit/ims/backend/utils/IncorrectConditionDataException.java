package pl.sgnit.ims.backend.utils;

import java.lang.reflect.Field;

public class IncorrectConditionDataException extends RuntimeException {
    public IncorrectConditionDataException(Field field, String conditionData) {
        super(field.getName()+": Incorrect condition data - "+conditionData);
    }
}
