package pl.sgnit.ims.backend.calendar.weekscheduleday;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekScheduleDayRepository extends JpaRepository<WeekScheduleDay, Long> {

    List<WeekScheduleDay> findAllByWeekScheduleId(Long weekScheduleId);

    void deleteAllByWeekScheduleId(Long weekScheduleId);
}
