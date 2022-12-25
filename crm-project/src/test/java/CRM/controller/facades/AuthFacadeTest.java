package CRM.controller.facades;

import CRM.entity.User;
import CRM.entity.requests.LoginUserRequest;
import CRM.entity.requests.RegisterUserRequest;
import CRM.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;


@ExtendWith(MockitoExtension.class)
public class AuthFacadeTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthFacade authFacade;

    private RegisterUserRequest correctRegisterUserRequest;
    private LoginUserRequest correctLoginUserRequest;

//    @BeforeEach
//    void setUp() {
//        correctLoginUserRequest = new LoginUserRequest("test@gmail.com", "ziv123456");
//        correctRegisterUserRequest = new RegisterUserRequest("Ziv", "Hausler", "ziv123456", "test@gmail.com");
//    }

    @Test
    @DisplayName("Test that validateRegisteredUser is called with correct arguments")
    public void testValidate_RegisteredUserCalled() {
        User user = User.newUser(correctRegisterUserRequest);
        user.setId(1L);
        given(authService.register(correctRegisterUserRequest)).willReturn(user);
        try {
            assertEquals(201, authFacade.register(correctRegisterUserRequest).getStatusCode());
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    @DisplayName("Test that a 201 status code and success message is returned when authService.register is successful")
    public void testSuccessResponseReturned_WhenAuthServiceRegisterIsSuccessful() {
        User user = User.newUser(correctRegisterUserRequest);
        user.setId(1L);

        given(authService.register(correctRegisterUserRequest)).willReturn(user);
        assertEquals(201, authFacade.register(correctRegisterUserRequest).getStatusCode());
    }

//    @Test
//    @DisplayName("Test that a 400 status code and error message is returned when Validations.validateRegisteredUser throws an IllegalArgumentException")
//    public void testErrorResponseReturned_WhenValidateRegisteredUserThrowsIllegalArgumentException() {
//        RegisterUserRequest incorrectEmailRegisterUserRequest = new RegisterUserRequest("Ziv", "Hausler", "56", "testgmail.com");
//        assertEquals(400, authFacade.register(incorrectEmailRegisterUserRequest).getStatusCode());
//    }

    @Test
    @DisplayName("Test that a 500 status code and error message is returned when Validations.validateRegisteredUser throws a NullPointerException")
    public void testErrorResponseReturned_WhenValidateRegisteredUserThrowsNullPointerException() {
        RegisterUserRequest nullRegisterUserRequest = new RegisterUserRequest();
        assertEquals(500, authFacade.register(nullRegisterUserRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test that authService.login is called with correct parameters")
    public void testAuthService_LoginIsCalled_WithCorrectParameters() {
        try {
            given(authService.login(correctLoginUserRequest)).willReturn("token");
            assertEquals(200, authFacade.login(correctLoginUserRequest).getStatusCode());
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    @DisplayName("Test that a 400 status code is returned when Validations.validateLoginUser throws an IllegalArgumentException")
    public void testErrorResponseReturned_WhenValidateLoginUserThrowsIllegalArgumentException() throws AuthenticationException, AccountNotFoundException {
        given(authService.login(correctLoginUserRequest)).willThrow(IllegalArgumentException.class);
        assertEquals(400, authFacade.login(correctLoginUserRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test that a 400 status code is returned when authService.login throws an AuthenticationException")
    public void testErrorResponseReturned_WhenAuthServiceLoginThrowsAuthenticationException() throws AuthenticationException, AccountNotFoundException {
        given(authService.login(correctLoginUserRequest)).willThrow(AuthenticationException.class);
        assertEquals(401, authFacade.login(correctLoginUserRequest).getStatusCode());
    }

    @Test
    @DisplayName("Test that a 500 status code is returned when authService.login throws a NullPointerException")
    public void testErrorResponseReturned_WhenAuthServiceLoginThrowsNullPointerException() throws AuthenticationException, AccountNotFoundException {
        given(authService.login(correctLoginUserRequest)).willThrow(NullPointerException.class);
        assertEquals(500, authFacade.login(correctLoginUserRequest).getStatusCode());
    }
}
