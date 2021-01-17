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
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends EntityTemplate {

    @Column(name="username", length = 32, nullable = false)
    @QueryableField
    private String username;

    @Column(length = 511, nullable = false)
    private String password;

    @Column(length = 32, nullable = false)
    private String salt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> grantedRoles;

    @Column(name="first_name")
    @QueryableField
    private String firstName;

    @Column(name="last_name")
    @QueryableField
    private String lastName;

    @Column(name="email")
    @QueryableField
    private String email;

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

    private void hashPassword(String plainPassword) {
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
        this.email = email;
    }
}
