package CRM.filter;

import CRM.service.AuthService;
import CRM.utils.enums.ExceptionMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static CRM.utils.Util.permissionPathsForAll;

@Component
public class AuthorizationFilter extends GenericFilterBean {

    @Autowired
    AuthService authService;

    /**
     * this doFilter function is set to check if the user has the permission to enter the app controllers.
     * checks if the request was according to what we need with token in the authorization Header.
     *
     * @param request  - request from client
     * @param response - response if the action can be done or not.
     * @param chain    - chain of filters to go through
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("in AuthorizationFilter -> doFilter");
//        String url = ((HttpServletRequest) request).getRequestURL().toString();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = httpRequest.getHeader("authorization");
        String path = httpRequest.getRequestURI();
        if (permissionPathsForAll.stream().noneMatch(path::contains)) {
            try {
                Long userId = authService.checkTokenToUserInDB(token);
                httpRequest.setAttribute("userId", userId);
            } catch (AccountNotFoundException e) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}