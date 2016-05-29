package by.bsu.famcs.drapegnik;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.bsu.famcs.drapegnik.LoginServlet.FILE_NAME;

/**
 * Created by Drapegnik on 29.05.16.
 */
@WebFilter(value = {"/homepage.jsp", "/homepage"})
public class ChatFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        (new LoginServlet()).init();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uidParam = request.getParameter(LoginServlet.COOKIE_USER_ID);
        if (uidParam == null && request instanceof HttpServletRequest) {
            Cookie[] cookies = ((HttpServletRequest) request).getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(LoginServlet.COOKIE_USER_ID)) {
                    uidParam = cookie.getValue();
                }
            }
        }
        boolean authenticated = checkAuthenticated(uidParam);
        if (authenticated) {
            chain.doFilter(request, response);
        } else if (response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).sendRedirect("/login.jsp");
        } else {
            response.getOutputStream().println("403, Forbidden");
        }

    }

    private boolean checkAuthenticated(String uid) {
        return uid != null && UserHandler.getUserById(uid) != null;
    }

    @Override
    public void destroy() {

    }
}
