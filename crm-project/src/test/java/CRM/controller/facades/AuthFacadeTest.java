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

//    @Test
//    @DisplayName("Test that a 200 status code and success message is returned when authService.register is successful")
//    public void successResponseReturned__Successful() {
//        RegisterUserRequest correctRegisterUserRequest = new RegisterUserRequest("Ziv Hausler", "ziv123456", "test@gmail.com");
//        User user = User.newUser(correctRegisterUserRequest);
//        user.setId(1L);
//        given(authService.register(correctRegisterUserRequest)).willReturn(user);
//        assertEquals(HttpStatusCodes.STATUS_CODE_OK, authFacade.register(correctRegisterUserRequest).getStatusCode());
//    }

    @Test
    @DisplayName("Test that a 400 status code and error message is returned when Validations.validateRegisteredUser throws an IllegalArgumentException")
    public void register_InvalidPassword_BadRequestResponse() {
        RegisterUserRequest incorrectEmailRegisterUserRequest = new RegisterUserRequest("ZivHausler", "56", "testgmail.com");
        assertEquals(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, authFacade.register(incorrectEmailRegisterUserRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test that a 500 status code and error message is returned when Validations.validateRegisteredUser throws a NullPointerException")
    public void register_NullInput_ServerErrorResponse() {
        RegisterUserRequest nullRegisterUserRequest = new RegisterUserRequest();
        assertEquals(HttpStatusCodes.STATUS_CODE_SERVER_ERROR, authFacade.register(nullRegisterUserRequest).getStatusCode());
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
    public void login_ServiceThrowsIllegalArgument_BadRequestResponse() throws AuthenticationException, AccountNotFoundException {
        LoginUserRequest correctLoginUserRequest = new LoginUserRequest("test@gmail.com", "ziv123456");
        given(authService.login(correctLoginUserRequest)).willThrow(IllegalArgumentException.class);
        assertEquals(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, authFacade.login(correctLoginUserRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test that a 400 status code is returned when authService.login throws an AuthenticationException")
    public void login_ServiceThrowsAuthenticationException_UnauthorizedResponse() throws AuthenticationException, AccountNotFoundException {
        LoginUserRequest correctLoginUserRequest = new LoginUserRequest("test@gmail.com", "ziv123456");
        given(authService.login(correctLoginUserRequest)).willThrow(AuthenticationException.class);
        assertEquals(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED, authFacade.login(correctLoginUserRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test that a 500 status code is returned when authService.login throws a NullPointerException")
    public void login_NullPassword_ServerErrorResponse() throws AuthenticationException, AccountNotFoundException {
        LoginUserRequest correctLoginUserRequest = new LoginUserRequest("test@gmail.com", null);
        assertEquals(HttpStatusCodes.STATUS_CODE_SERVER_ERROR, authFacade.login(correctLoginUserRequest).getStatusCode());
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
    public void thirdPartyLogin_UnsuccessfulLogin_BadRequestStatus() throws IOException {
        String code = "someCode";
        given(githubCodeDecoder.getUserDataFromCode(code)).willThrow(new IOException());
        assertEquals(HttpStatusCodes.STATUS_CODE_BAD_REQUEST, authFacade.thirdPartyLogin(code).getStatusCode());
    }

    @Test
    @DisplayName("thirdPartyLogin - Unsuccessful login - NullPointerException")
    public void thirdPartyLogin_UnsuccessfulLogin_ServerErrorStatus() throws IOException {
        String code = "someCode";
        given(githubCodeDecoder.getUserDataFromCode(code)).willThrow(new NullPointerException());
        assertEquals(HttpStatusCodes.STATUS_CODE_SERVER_ERROR, authFacade.thirdPartyLogin(code).getStatusCode());
    }
}
