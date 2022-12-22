package CRM.entity;

import CRM.entity.requests.RegisterUserRequest;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String firstName;
    private String lastName;
    private String password;
    private String email;

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Comment> comments = new HashSet<>();

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Item> items = new HashSet<>();

    public static User newUser(RegisterUserRequest registerUser){
        User user = new User();
        user.setEmail(registerUser.getEmail());
        user.setPassword(registerUser.getPassword());
        user.setFirstName(registerUser.getFirstName());
        user.setLastName(registerUser.getLastName());
        return user;
    }

//    public void addBoard(Board board){
//        boards.add(board);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, password, email);
    }
}
