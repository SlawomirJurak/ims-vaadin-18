package pl.sgnit.ims.backend.administration.contentpanel;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.sgnit.ims.views.administration.role.RolesView;
import pl.sgnit.ims.views.administration.user.UsersView;
import pl.sgnit.ims.views.calendar.weekscedule.WeekScheduleView;
import pl.sgnit.ims.views.util.ViewConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContentPanelService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final ContentPanelRepository contentPanelRepository;

    private List<Class<? extends Component>> contentPanelsClasses;

    public ContentPanelService(ContentPanelRepository contentPanelRepository) {
        this.contentPanelRepository = contentPanelRepository;

        contentPanelsClasses = Arrays.asList(
            RolesView.class,
            UsersView.class,
            WeekScheduleView.class
        );

    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkViewsInDatabase() {
        checkUniquenessPanelNames();
        contentPanelsClasses.stream()
            .forEach(this::checkContentPanel);
    }

    private void checkUniquenessPanelNames() {
        Map<String, Integer> contentPanelsNames = new HashMap<>();

        contentPanelsClasses.stream().map(panelClass -> {
            return panelClass.getAnnotation(ViewConfiguration.class).id();
        }).forEach(name -> {
            if (contentPanelsNames.containsKey(name)) {
                int count = contentPanelsNames.get(name);

                contentPanelsNames.put(name, ++count);
                return;
            }
            contentPanelsNames.put(name, 1);
        });

        String nonUniquePanels = contentPanelsNames.entrySet().stream()
            .filter(entry -> entry.getValue()>1)
            .map(entry -> {
                return entry.getKey()+"="+entry.getValue();
            }).collect(Collectors.joining(","));
        if (!nonUniquePanels.isEmpty()) {
            throw new RuntimeException("Non unique panels: "+nonUniquePanels);
        }
        logger.info("Uniqueness of panels checked");
    }

    private void checkContentPanel(Class<? extends Component> contentPanelClass) {
        ViewConfiguration viewConfiguration = contentPanelClass.getAnnotation(ViewConfiguration.class);
        String id = viewConfiguration.id();
        String description = viewConfiguration.description();
        String title = viewConfiguration.title();
        Optional<ContentPanel> optionalContentPanel = contentPanelRepository.findByViewId(id);
        ContentPanel contentPanel;

        if (optionalContentPanel.isPresent()) {
            contentPanel = optionalContentPanel.get();

            contentPanel.setViewId(id);
            contentPanel.setTitle(title);
            contentPanel.setDescription(description);
        } else {
            contentPanel = new ContentPanel(
                id,
                title,
                description
            );
        }
        contentPanelRepository.save(contentPanel);
    }

    public Set<ContentPanel> findAll() {
        return Set.copyOf(contentPanelRepository.findAll());
    }
}
