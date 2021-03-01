package pl.sgnit.ims.backend.calendar.weekscheduleday;

import pl.sgnit.ims.backend.EntityTemplate;

import javax.persistence.Convert;
import javax.persistence.Entity;

@Entity
public class WeekScheduleDay extends EntityTemplate {

    private Long weekScheduleId;

    @Convert(converter = DayOfWeekConverter.class)
    private DayOfWeek dayOfWeek;

    @Convert(converter = DayTypeConverter.class)
    private DayType dayType;

    public WeekScheduleDay() {
    }

    public WeekScheduleDay(Long weekScheduleId, DayOfWeek dayOfWeek, DayType dayType) {
        this.weekScheduleId = weekScheduleId;
        this.dayOfWeek = dayOfWeek;
        this.dayType = dayType;
    }

    public Long getWeekScheduleId() {
        return weekScheduleId;
    }

    public void setWeekScheduleId(Long weekScheduleId) {
        this.weekScheduleId = weekScheduleId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }
}
