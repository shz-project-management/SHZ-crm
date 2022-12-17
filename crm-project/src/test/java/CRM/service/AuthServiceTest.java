package CRM.service;

import CRM.entity.User;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.repository.UserRepository;
import CRM.utils.enums.ExceptionMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Test register method with valid input")
    void testRegisterWithValidInput() {
        RegisterUserRequest userRequest = new RegisterUserRequest("Ziv", "Hausler", "ziv123456", "ziv@gmail.com");
        given(userRepository.findByEmail(userRequest.getEmail())).willReturn(Optional.empty());
        given(userRepository.save(User.newUser(userRequest))).willReturn(User.newUser(userRequest));
        assertEquals(userRequest.getEmail(), authService.register(userRequest).getEmail());
    }

    @Test
    @DisplayName("Test register method when email is already in use")
    void testRegisterWithEmailInUse() {
        RegisterUserRequest userRequest = new RegisterUserRequest("Ziv", "Hausler", "ziv123456", "ziv@gmail.com");
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(new User()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.register(userRequest));
        assertEquals(ExceptionMessage.EMAIL_IN_USE.toString(), exception.getMessage());

        verify(userRepository).findByEmail(userRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Test login method with valid input")
    void testLoginWithValidInput() throws AuthenticationException, AccountNotFoundException {
        RegisterUserRequest userRequest = new RegisterUserRequest("Ziv", "Hausler", "ziv123456", "ziv@gmail.com");
        User storedUser = User.newUser(userRequest);
        LoginUserRequest loginUser = new LoginUserRequest("ziv@gmail.com", "ziv123456");
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(storedUser));

        String token = authService.login(loginUser);

        assertNotNull(token);
        verify(userRepository).findByEmail(userRequest.getEmail());
    }

    @Test
    @DisplayName("Test login method when email is not found")
    void testLoginWithEmailNotFound() {
        LoginUserRequest loginUser = new LoginUserRequest("ziv@gmail.com", "ziv123456");
        when(userRepository.findByEmail(loginUser.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () -> authService.login(loginUser));
        assertEquals(ExceptionMessage.ACCOUNT_DOES_NOT_EXISTS.toString(), exception.getMessage());

        verify(userRepository).findByEmail(loginUser.getEmail());
    }

    @Test
    @DisplayName("Test login method when password does not match")
    void testLoginWithIncorrectPassword() {
        LoginUserRequest loginUser = new LoginUserRequest("ziv@gmail.com", "ziv123456");
        User storedUser = new User(1L, "Ziv", "Hausler", "not-the-same-password-for-sure", "ziv@gmail.com", null, null);
        when(userRepository.findByEmail(loginUser.getEmail())).thenReturn(Optional.of(storedUser));

        Exception exception = assertThrows(AuthenticationException.class, () -> authService.login(loginUser));
        assertEquals(ExceptionMessage.PASSWORD_DOESNT_MATCH.toString(), exception.getMessage());

        verify(userRepository).findByEmail(loginUser.getEmail());
    }
}
