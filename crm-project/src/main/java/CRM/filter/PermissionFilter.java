//package CRM.filter;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.GenericFilterBean;
//import org.springframework.web.server.ResponseStatusException;
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
//
//@Component
//public class PermissionFilter extends GenericFilterBean {
//
//    private static Logger logger = LogManager.getLogger(PermissionFilter.class.getName());
////    @Autowired
////    AuthService authService;
////    @Autowired
////    UserDocumentRepository userDocumentRepository;
////    @Autowired
////    UserRepository userRepository;
////    @Autowired
////    DocumentRepository documentRepository;
////    @Autowired
////    FolderRepository folderRepository;
//
//    /**
//     * this doFilter function is set to check if the user has the permission to do the action he
//     * wanted, according to his role that saved in the userDocumentRepository, this repo has the information
//     * about all document and all the users that watch each document and his role.
//     * we split the URI of the request into a list of string and make the checks based on the URI.
//     *
//     * @param request  - request from client
//     * @param response - response if the action can be done or not.
//     * @param chain    - chain of filters to go through
//     * @throws IOException      -
//     * @throws ServletException -
//     */
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  {
//        logger.info("in PermissionFilter -> doFilter");
//        String url = ((HttpServletRequest) request).getRequestURL().toString();
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        // make sure the user is allowed to create this action, e.g.:
//        // if the user is not admin but tries to delete the board, deny,
//        // if the user is a leader and tries to create item, allow.
//        // ...
//        // make sure you keep the filter as simple as possible, and the main function (doFilter) should contain only
//        // a few lines of code. If you need more space, move this code into an external function and call it here!
//    }
//}
