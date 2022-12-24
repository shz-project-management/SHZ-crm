package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginUserRequest {
    private String email;
    private String password;
}
