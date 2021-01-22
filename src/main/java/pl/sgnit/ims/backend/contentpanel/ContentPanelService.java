package pl.sgnit.ims.backend.contentpanel;

import com.vaadin.flow.component.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.sgnit.ims.views.about.AboutView;
import pl.sgnit.ims.views.role.RolesView;
import pl.sgnit.ims.views.user.UsersView;
import pl.sgnit.ims.views.util.ViewPath;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ContentPanelService {

    private final ContentPanelRepository contentPanelRepository;

    private List<Class<? extends Component>> contentPanelsClasses;

    public ContentPanelService(ContentPanelRepository contentPanelRepository) {
        this.contentPanelRepository = contentPanelRepository;

        contentPanelsClasses = Arrays.asList(
            RolesView.class,
            UsersView.class
        );

    }

    @EventListener(ApplicationReadyEvent.class)
    public void checkViewsInDatabase() {
        contentPanelsClasses.stream()
            .forEach(this::checkContentPanel);
    }

    private void checkContentPanel(Class<? extends Component> contentPanelClass) {
        String contentPanelPath = contentPanelClass.getAnnotation(ViewPath.class).value();
        Optional<ContentPanel> optionalContentPanel = contentPanelRepository.findByPath(contentPanelPath);

        if (!optionalContentPanel.isPresent()) {
            ContentPanel contentPanel = new ContentPanel(
                contentPanelClass.getSimpleName(),
                contentPanelPath,
                null
            );
            contentPanelRepository.save(contentPanel);
        }
    }

    public Set<ContentPanel> findAll() {
        return Set.copyOf(contentPanelRepository.findAll());
    }
}
