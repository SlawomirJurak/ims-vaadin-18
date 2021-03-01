package pl.sgnit.ims.backend.calendar.weekscheduleday;

import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeekScheduleDayService {

    private final WeekScheduleDayRepository weekScheduleDayRepository;

    public WeekScheduleDayService(WeekScheduleDayRepository weekScheduleDayRepository) {
        this.weekScheduleDayRepository = weekScheduleDayRepository;
    }

    public void createWeekSchedule(Long weekScheduleId) {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            WeekScheduleDay weekScheduleDay = new WeekScheduleDay(weekScheduleId, dayOfWeek, null);

            weekScheduleDayRepository.save(weekScheduleDay);
        }
    }

    public List<WeekScheduleDay> getEmptyWeekSchedule() {
        List<WeekScheduleDay> weekScheduleDayList = new ArrayList<>();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            WeekScheduleDay weekScheduleDay = new WeekScheduleDay(null, dayOfWeek, null);

            weekScheduleDayList.add(weekScheduleDay);
        }
        return weekScheduleDayList;
    }

    public List<WeekScheduleDay> getWeekScheduleByWeekScheduleId(Long weekScheduleId) {
        return weekScheduleDayRepository.findAllByWeekScheduleId(weekScheduleId);
    }

    public void saveWeekDaysData(List<WeekScheduleDay> weekScheduleDayList) {
        weekScheduleDayList.forEach(weekScheduleDayRepository::save);
    }

    public void deleteWeekSchedule(Long weekScheduleId) {
        weekScheduleDayRepository.deleteAllByWeekScheduleId(weekScheduleId);
    }
}
