package pl.sgnit.ims.backend.calendar.weekschedule;


import pl.sgnit.ims.backend.EntityTemplate;
import pl.sgnit.ims.backend.utils.QueryableField;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class WeekSchedule extends EntityTemplate {

    @Column(nullable = false, length = 200)
    @QueryableField
    private String description;

    public WeekSchedule() {
    }

    public WeekSchedule(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
