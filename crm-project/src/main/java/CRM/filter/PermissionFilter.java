//package CRM.filter;
//
//import CRM.entity.Board;
//import CRM.entity.User;
//import CRM.repository.BoardRepository;
//import CRM.service.AuthService;
//import CRM.service.BoardService;
//import CRM.utils.enums.Permission;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.naming.NoPermissionException;
//import javax.security.auth.login.AccountNotFoundException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//
//import static CRM.utils.Util.*;
//
//
//@Component
//public class PermissionFilter extends GenericFilterBean {
//    @Autowired
//    private BoardRepository boardRepository;
//
//    private static Logger logger = LogManager.getLogger(PermissionFilter.class.getName());
//    @Autowired
//    AuthService authService;
//    @Autowired
//    BoardService boardService;
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
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        logger.info("in AuthorizationFilter -> doFilter");
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String token = httpRequest.getHeader("authorization");
//        String boardId = httpRequest.getHeader("boardId");
//        String path = httpRequest.getRequestURI();
//
//        if (isPermittedPath(path)) {
//            Board board;
//            User user;
//            Permission permission;
//
//            try {
//                Long userId = authService.checkTokenToUserInDB(token);
//                board = boardService.get(Long.parseLong(boardId));
//                user = authService.findById(userId);
//                permission = board.getUserPermissionWithoutAdminByUserId(user.getId());
//            } catch (AccountNotFoundException | NoPermissionException e) {
//                sendForbiddenResponse(httpResponse);
//                return;
//            }
//
//            if (permission.equals(Permission.ADMIN) || isValidRequest(httpRequest, path, permission)) {
//                chain.doFilter(request, response);
//            } else {
//                sendForbiddenResponse(httpResponse);
//            }
//        } else {
//            chain.doFilter(request, response);
//        }
//    }
//
//    private boolean isPermittedPath(String path) {
//        return permissionPathsForAll.stream().noneMatch(path::contains);
//    }
//
//    private boolean isValidRequest(HttpServletRequest httpRequest, String path, Permission permission) throws IOException {
//        if (permission == Permission.LEADER) {
//            return isPermittedForLeaders(path) && isValidUpdateForLeaders(httpRequest);
//        }
//        return isPermittedForUsers(path) && isValidUpdateForUsers(httpRequest, path);
//    }
//
//    private boolean isValidUpdateForLeaders(HttpServletRequest httpRequest) throws IOException {
//        if (httpRequest.getMethod().equals("POST")) {
//            return true;
//        } else if (httpRequest.getMethod().equals("PATCH")) {
//            String jsonBody = createBody(httpRequest);
//            Gson gson = new Gson();
//            JsonObject body = gson.fromJson(jsonBody, JsonObject.class);
//            String fieldName = body.get("fieldName").getAsString();
//            return fieldName.equals("STATUS") || fieldName.equals("TYPE");
//        }
//        return false;
//    }
//
//    private boolean isPermittedForUsers(String path) {
//        return permissionPathsForUsers.stream().anyMatch(path::contains);
//    }
//
//    private boolean isPermittedForLeaders(String path) {
//        return permissionPathsForLeaders.stream().anyMatch(path::contains);
//    }
//
//    private boolean isValidUpdateForUsers(HttpServletRequest httpRequest, String path) throws IOException {
//        if (httpRequest.getMethod().equals("POST") && path.contains("comment")) {
//            return true;
//        } else if (httpRequest.getMethod().equals("PATCH") && path.contains("item")) {
//            String jsonBody = createBody(httpRequest);
//            Gson gson = new Gson();
//            JsonObject body = gson.fromJson(jsonBody, JsonObject.class);
//            String fieldName = body.get("fieldName").getAsString();
//            return fieldName.equals("STATUS") || fieldName.equals("TYPE");
//        }
//        return false;
//    }
//
//    private void sendForbiddenResponse(HttpServletResponse httpResponse) throws IOException {
//        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
//    }
//
//    private String createBody(HttpServletRequest httpRequest) throws IOException {
//        BufferedReader reader = httpRequest.getReader();
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//        return sb.toString();
//    }
//}
