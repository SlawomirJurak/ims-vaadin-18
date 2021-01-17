package pl.sgnit.ims.backend.contentpanel;

import pl.sgnit.ims.backend.EntityTemplate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "content_panels")
public class ContentPanel extends EntityTemplate {

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String path;

    @Column(length = 4096)
    private String description;

    public ContentPanel() {
    }

    public ContentPanel(String name, String path, String description) {
        this.name = name;
        this.path = path;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
