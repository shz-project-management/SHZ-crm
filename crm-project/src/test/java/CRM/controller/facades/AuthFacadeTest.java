package CRM.controller.facades;

import CRM.entity.User;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.service.AuthService;
import CRM.utils.GithubCodeDecoder;
import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;


@ExtendWith(MockitoExtension.class)
public class AuthFacadeTest {

    @Mock
    private AuthService authService;
    @Mock
    private GithubCodeDecoder githubCodeDecoder;
    @InjectMocks
    private AuthFacade authFacade;


    @Test
    @DisplayName("Test that validateRegisteredUser is called with correct arguments")
    public void register_ValidInput_Success() {
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        User user = User.newUser(correctRegisterUserRequest);
        user.setId(1L);

        given(authService.register(correctRegisterUserRequest)).willReturn(user);

        assertEquals(HttpStatusCodes.STATUS_CODE_OK, authFacade.register(correctRegisterUserRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test that a 400 status code and error message is returned when Validations.validateRegisteredUser throws an IllegalArgumentException")
    public void register_InvalidPassword_ThrowsIllegalArgumentException() {
        RegisterUserRequest incorrectEmailRegisterUserRequest = new RegisterUserRequest("ZivHausler", "56", "testgmail.com");
        assertThrows(IllegalArgumentException.class, () -> authFacade.register(incorrectEmailRegisterUserRequest));
    }

    @Test
    @DisplayName("Test that a 500 status code and error message is returned when Validations.validateRegisteredUser throws a NullPointerException")
    public void register_NullInput_ThrowsNullPointerException() {
        RegisterUserRequest nullRegisterUserRequest = new RegisterUserRequest();
        assertThrows(NullPointerException.class, () -> authFacade.register(nullRegisterUserRequest));
    }

    @Test
    @DisplayName("Test that authService.login is called with correct parameters")
    public void login_ValidInput_Success() throws AuthenticationException, AccountNotFoundException {
        LoginUserRequest correctLoginUserRequest = new LoginUserRequest("test@gmail.com", "ziv123456");
        given(authService.login(correctLoginUserRequest)).willReturn("token");
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, authFacade.login(correctLoginUserRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test that a 400 status code is returned when Validations.validateLoginUser throws an IllegalArgumentException")
    public void login_ServiceThrowsIllegalArgument_ThrowsIllegalArgumentException() throws AuthenticationException, AccountNotFoundException {
        LoginUserRequest correctLoginUserRequest = new LoginUserRequest("test@gmail.com", "ziv123456");
        given(authService.login(correctLoginUserRequest)).willThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> authFacade.login(correctLoginUserRequest));
    }

    @Test
    @DisplayName("Test that a 400 status code is returned when authService.login throws an AuthenticationException")
    public void login_ServiceThrowsAuthenticationException_ThrowsUnauthorizedException() throws AuthenticationException, AccountNotFoundException {
        LoginUserRequest correctLoginUserRequest = new LoginUserRequest("test@gmail.com", "ziv123456");
        given(authService.login(correctLoginUserRequest)).willThrow(AuthenticationException.class);
        assertThrows(AuthenticationException.class, () -> authFacade.login(correctLoginUserRequest));
    }

    @Test
    @DisplayName("Test that a 500 status code is returned when authService.login throws a NullPointerException")
    public void login_NullPassword_ThrowsNullPointerException() {
        LoginUserRequest correctLoginUserRequest = new LoginUserRequest("test@gmail.com", null);
        assertThrows(NullPointerException.class, () -> authFacade.login(correctLoginUserRequest));
    }

    @Test
    @DisplayName("thirdPartyLogin - Successful login")
    public void thirdPartyLogin_SuccessfulLogin_OkStatus() throws IOException {
        String code = "someCode";
        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
        given(githubCodeDecoder.getUserDataFromCode(code)).willReturn(correctRegisterUserRequest);
        given(authService.thirdPartyLogin(correctRegisterUserRequest)).willReturn("token");
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, authFacade.thirdPartyLogin(code).getStatusCode());
    }

    @Test
    @DisplayName("thirdPartyLogin - Unsuccessful login - IOException")
    public void thirdPartyLogin_UnsuccessfulLogin_ThrowsIOException() throws IOException {
        String code = "someCode";
        given(githubCodeDecoder.getUserDataFromCode(code)).willThrow(new IOException());
        assertThrows(IOException.class, () -> authFacade.thirdPartyLogin(code));

    }

    @Test
    @DisplayName("thirdPartyLogin - Unsuccessful login - NullPointerException")
    public void thirdPartyLogin_UnsuccessfulLogin_ThrowsNullPointerException() throws IOException {
        String code = "someCode";
        given(githubCodeDecoder.getUserDataFromCode(code)).willThrow(new NullPointerException());
        assertThrows(NullPointerException.class, () -> authFacade.thirdPartyLogin(code));
    }
}
