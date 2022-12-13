//package CRM.filter;
//
//import CRM.utils.enums.ExceptionMessage;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.security.auth.login.AccountNotFoundException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class AuthorizationFilter extends GenericFilterBean {
//
////    @Autowired
////    AuthService authService;
//
//    /**
//     * this doFilter function is set to check if the user has the permission to enter the app controllers.
//     * checks if the request was according to what we need with token in the authorization Header.
//     *
//     * @param request  - request from client
//     * @param response - response if the action can be done or not.
//     * @param chain    - chain of filters to go through
//     */
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        logger.info("in AuthorizationFilter -> doFilter");
//        String url = ((HttpServletRequest) request).getRequestURL().toString();
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        // create a list of things that are authorized to pass the filters (such as option method) and let them through.
//
//        // if it's not one of those, get the token from the request and parse it to userId using authService.
//        // of course, make sure this user exists in the db.
//
//        // if found, let through. else, kick out with a res.SendError() function.
//
//        chain.doFilter(request, response);
//    }
//}