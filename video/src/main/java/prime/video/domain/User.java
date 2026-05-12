package prime.video.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String role;

    public User() {}

    public User(UUID id, String email, String password, String country, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.country = country;
        this.role = role;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public static class UserBuilder {
        private UUID id;
        private String email;
        private String password;
        private String country;
        private String role;

        public UserBuilder id(UUID id) { this.id = id; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder country(String country) { this.country = country; return this; }
        public UserBuilder role(String role) { this.role = role; return this; }
        public User build() { return new User(id, email, password, country, role); }
    }
}
