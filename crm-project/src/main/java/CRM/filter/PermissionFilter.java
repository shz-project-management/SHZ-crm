package CRM.filter;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.repository.BoardRepository;
import CRM.repository.UserRepository;
import CRM.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static CRM.utils.Util.*;


@Component
public class PermissionFilter extends GenericFilterBean {
    @Autowired
    private BoardRepository boardRepository;

    private static Logger logger = LogManager.getLogger(PermissionFilter.class.getName());
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;

    /**
     * this doFilter function is set to check if the user has the permission to do the action he
     * wanted, according to his role that saved in the userDocumentRepository, this repo has the information
     * about all document and all the users that watch each document and his role.
     * we split the URI of the request into a list of string and make the checks based on the URI.
     *
     * @param request  - request from client
     * @param response - response if the action can be done or not.
     * @param chain    - chain of filters to go through
     * @throws IOException      -
     * @throws ServletException -
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("in AuthorizationFilter -> doFilter");
//        String url = ((HttpServletRequest) request).getRequestURL().toString();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = httpRequest.getParameter("token");
        String path = httpRequest.getRequestURI();
        if (permissionPathsForAll.stream().noneMatch(path::contains)) {
            User user;
            try {
                Long userId = authService.checkTokenToUserInDB(token);
                user = authService.findById(userId);
            } catch (AccountNotFoundException e) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if (permissionPathsForRegisters.stream().noneMatch(path::contains)) {

            }

        }
        chain.doFilter(request, response);
    }
}
