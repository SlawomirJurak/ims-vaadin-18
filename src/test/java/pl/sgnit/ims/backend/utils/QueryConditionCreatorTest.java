package pl.sgnit.ims.backend.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryConditionCreatorTest {

    @ParameterizedTest
    @CsvSource(value = {
        "stringField^admin^u.stringField = 'admin'",
        "stringField^>admin^u.stringField > 'admin'",
        "stringField^>=admin^u.stringField >= 'admin'",
        "stringField^<=admin^u.stringField <= 'admin'",
        "integerField^2^u.integerField = 2",
        "integerField^>2^u.integerField > 2",
        "integerField^>=2^u.integerField >= 2",
        "integerField^<=2^u.integerField <= 2",
        "integerField^2..10^u.integerField between 2 and 10",
        "booleanField^true^u.booleanField = true",
        "booleanField^false^u.booleanField = false",
        "dateField^2020-02-02^u.dateField = '2020-02-02'",
        "dateField^2020-02-02..2021-02-02^u.dateField between '2020-02-02' and '2021-02-02'",
        "stringField^get%^u.stringField like 'get%'",
        "integerField^2%^to_char(u.integerField, 'FM9999999999') like '2%'",
        "dateField^2020-02%^to_char(u.dateField, 'YYYY-MM-DD') like '2020-02%'",
        "stringField^get;set^(u.stringField = 'get' or u.stringField = 'set')",
        "integerField^!^u.integerField is null",
        "integerField^!!^u.integerField is not null"
    }, delimiter = '^')
    void oneFieldConditionTests(String fieldName, String condition, String conditionString) throws NoSuchFieldException {
        Field declaredField = TestClass.class.getDeclaredField(fieldName);
        Map<Field, String> map = new HashMap<>();

        map.put(declaredField, condition);
        String preparedCondition = QueryConditionCreator.prepareQueryCondition(map, "u");

        assertEquals(conditionString, preparedCondition);

    }

    @ParameterizedTest
    @CsvSource({
        "stringField,admin,integerField,2,u.stringField = 'admin' and u.integerField = 2",
        "stringField,ad%,integerField,>2,u.stringField like 'ad%' and u.integerField > 2",
        "dateField,2021-02-01,integerField,<=2,u.dateField = '2021-02-01' and u.integerField <= 2"
    })
    void twoFieldsConditionTest(ArgumentsAccessor arguments) throws NoSuchFieldException  {
        Field firstField = TestClass.class.getDeclaredField(arguments.getString(0));
        Field secondField = TestClass.class.getDeclaredField(arguments.getString(2));
        Map<Field, String> map = new HashMap<>();

        map.put(firstField, arguments.getString(1));
        map.put(secondField, arguments.getString(3));

        String preparedCondition = QueryConditionCreator.prepareQueryCondition(map, "u");

        assertEquals(arguments.getString(4), preparedCondition);
    }

    static class TestClass {

        @Column(name = "integerField")
        Integer integerField;

        @Column(name = "longField")
        Long longField;

        @Column(name = "stringField")
        String stringField;

        @Column(name = "booleanField")
        Boolean booleanField;

        @Column(name = "dateField")
        LocalDate dateField;
    }
}
