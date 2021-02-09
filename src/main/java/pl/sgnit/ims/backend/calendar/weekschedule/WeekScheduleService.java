package pl.sgnit.ims.backend.calendar.weekschedule;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeekScheduleService {

    private final WeekScheduleRepository weekScheduleRepository;

    WeekScheduleService(WeekScheduleRepository weekScheduleRepository) {
        this.weekScheduleRepository = weekScheduleRepository;
    }

    public List<WeekSchedule> findAll() {
        return weekScheduleRepository.findAll();
    }

    public WeekSchedule save(WeekSchedule weekSchedule) {
        return weekScheduleRepository.save(weekSchedule);
    }

    public void deleteWeekSchedule(WeekSchedule weekSchedule) {
        weekScheduleRepository.delete(weekSchedule);
    }
}
