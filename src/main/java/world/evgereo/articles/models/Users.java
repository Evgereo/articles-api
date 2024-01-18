package world.evgereo.articles.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users implements UserDetails {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotBlank(message = "Name should be not empty or blank")
    @Size(min=2, max=100, message = "Size of your name too short or long")
    private String userName;

    @NotBlank(message = "Surname should be not empty or blank")
    @Size(min=2, max=100, message = "Size of your surname too short or long")
    private String userSurname;

    @Min(0)
    private int age;

    @NotBlank(message = "Email should be not empty")
    @Email(message = "Please provide a valid email address")
    @Size(max=150, message = "Maximum size of email is 150")
    private String email;

    @Size(min=2, max=100, message = "Size of your password too short or long")
    private String password;

    @Transient
    @Size(min=2, max=100, message = "Size of your password too short or long")
    private String passwordConfirm;

    @OneToMany(mappedBy = "author") // look for information on the cascade types // orphanRemoval = true, cascade = CascadeType.ALL
    private List<Articles> articles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return this.getUserName();
    }

    public String getUserName() {return userName;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}