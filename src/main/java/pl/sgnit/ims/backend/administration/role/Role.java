package pl.sgnit.ims.backend.administration.role;

import pl.sgnit.ims.backend.EntityTemplate;
import pl.sgnit.ims.backend.administration.contentpanel.ContentPanel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends EntityTemplate {

    @Column(length = 63, nullable = false)
    private String name;

    @Column(length = 511)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_content_panel", joinColumns = @JoinColumn(name = "panel_id"),
        inverseJoinColumns = @JoinColumn(name = "content_panel_id"))
    private Set<ContentPanel> contentPanels;

    public Role() {
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ContentPanel> getContentPanels() {
        return contentPanels;
    }

    public void setContentPanels(Set<ContentPanel> contentPanels) {
        this.contentPanels = contentPanels;
    }
}
