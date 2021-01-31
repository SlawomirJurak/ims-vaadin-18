package pl.sgnit.ims.backend.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import pl.sgnit.ims.backend.EntityTemplate;
import pl.sgnit.ims.backend.role.Role;
import pl.sgnit.ims.backend.utils.QueryableField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends EntityTemplate {

    @Column(name = "username", length = 32, nullable = false)
    @QueryableField
    private String username;

    @Column(length = 511)
    private String password;

    @Column(length = 32)
    private String salt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> grantedRoles;

    @Column(name = "first_name")
    @QueryableField
    private String firstName;

    @Column(name = "last_name")
    @QueryableField
    private String lastName;

    @Column(name = "email", unique = true)
    @QueryableField
    private String email;

    @Column(name = "active")
    @QueryableField
    private Boolean active;

    private String code;

    @Column(name = "administrator")
    @QueryableField
    private Boolean administrator;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        hashPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<Role> getGrantedRoles() {
        return grantedRoles;
    }

    public void setGrantedRoles(Set<Role> grantedRoles) {
        this.grantedRoles = grantedRoles;
    }

    public boolean checkPassword(String passwordToCheck) {
        return DigestUtils.sha512Hex(passwordToCheck + salt).equals(password);
    }

    public void hashPassword(String plainPassword) {
        salt = RandomStringUtils.random(32);
        password = DigestUtils.sha512Hex(plainPassword + salt);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public Boolean getActive() {
        return active == null ? Boolean.FALSE : active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void generateCode() {
        code = RandomStringUtils.randomAlphanumeric(32);
    }

    public Boolean getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator != null && administrator;
    }

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = false;
        }
        if (administrator == null) {
            administrator = false;
        }
    }
}
