package pl.sgnit.ims.backend.calendar.weekschedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sgnit.ims.backend.calendar.weekscheduleday.WeekScheduleDayService;

import java.util.List;

@Service
public class WeekScheduleService {

    private final WeekScheduleRepository weekScheduleRepository;
    private final WeekScheduleDayService weekScheduleDayService;

    WeekScheduleService(WeekScheduleRepository weekScheduleRepository, WeekScheduleDayService weekScheduleDayService) {
        this.weekScheduleRepository = weekScheduleRepository;
        this.weekScheduleDayService = weekScheduleDayService;
    }

    public List<WeekSchedule> findAll() {
        return weekScheduleRepository.findAll();
    }

    @Transactional
    public WeekSchedule save(WeekSchedule weekSchedule) {
        boolean isNewWeekSchedule = weekSchedule.getId()==null;

        WeekSchedule newWeekSchedule = weekScheduleRepository.save(weekSchedule);

        if (isNewWeekSchedule) {
            weekScheduleDayService.createWeekSchedule(newWeekSchedule.getId());
        }
        return newWeekSchedule;
    }

    @Transactional
    public void deleteWeekSchedule(WeekSchedule weekSchedule) {
        weekScheduleRepository.delete(weekSchedule);
        weekScheduleDayService.deleteWeekSchedule(weekSchedule.getId());
    }
}
