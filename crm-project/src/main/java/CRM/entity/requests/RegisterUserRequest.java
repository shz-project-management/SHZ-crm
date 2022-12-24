package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegisterUserRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
