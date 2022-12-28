package CRM.controller.controllers;

import CRM.entity.response.Response;
import com.google.api.client.http.HttpStatusCodes;
import org.hibernate.NonUniqueObjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import javax.naming.NoPermissionException;
import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class,
            NoSuchFieldException.class, NonUniqueObjectException.class, AccountNotFoundException.class
            , IOException.class, AuthenticationException.class, MailSendException.class})
    public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Response.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                .build());
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<Response> handleNoPermissionException(NoPermissionException ex) {
        return ResponseEntity.badRequest().body(Response.builder()
                .message(ex.getMessage())
                .status(HttpStatus.FORBIDDEN)
                .statusCode(HttpStatusCodes.STATUS_CODE_FORBIDDEN)
                .build());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Response> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity.badRequest().body(Response.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR)
                .build());
    }
}