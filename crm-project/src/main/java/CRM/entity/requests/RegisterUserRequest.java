package CRM.entity.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class RegisterUserRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
