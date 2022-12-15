package CRM.entity.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginUserRequest {
    private String email;
    private String password;
}
