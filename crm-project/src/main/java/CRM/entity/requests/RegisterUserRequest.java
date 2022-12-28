package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegisterUserRequest {
    private String fullName;
    private String password;
    private String email;
}
