package pl.sgnit.ims.backend.administration.contentpanel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentPanelRepository extends JpaRepository<ContentPanel, Long> {

    Optional<ContentPanel> findByViewId(String viewId);
}
