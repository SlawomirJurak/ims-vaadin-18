package pl.sgnit.ims.backend.administration.contentpanel;

import pl.sgnit.ims.backend.EntityTemplate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "content_panels")
public class ContentPanel extends EntityTemplate {

    @Column(length = 100, nullable = false, unique = true)
    private String viewId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 4096)
    private String description;

    public ContentPanel() {
    }

    public ContentPanel(String viewId, String title, String description) {
        this.viewId = viewId;
        this.title = title;
        this.description = description;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
