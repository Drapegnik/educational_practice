package by.bsu.famcs.drapegnik;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/login", initParams = {
        @WebInitParam(name = "cookie-live-time", value = "300")
})
public class LoginServlet extends HttpServlet {
    public static final String COOKIE_USER_ID = "uid";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "pass";
    public static final String PARAM_ERR_MESS = "errorMessage";
    public static final String FILE_NAME = "db.json";
    private int cookieLifeTime = -1;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cookieLifeTime = Integer.parseInt(config.getInitParameter("cookie-live-time"));
        String absolutePath = getServletContext().getRealPath("/");
        new UserHandler(absolutePath.substring(0, absolutePath.length() - 15) + FILE_NAME);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter(PARAM_NAME) == null || req.getParameter(PARAM_NAME).trim().isEmpty()
                || req.getParameter(PARAM_PASSWORD) == null || req.getParameter(PARAM_PASSWORD).trim().isEmpty()) {
            req.setAttribute(PARAM_ERR_MESS, "login/pass can't be empty!");
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        } else {

            User user = new User(req.getParameter(PARAM_NAME), req.getParameter(PARAM_PASSWORD));

            String userId = UserHandler.getUserId(user);
            System.out.println(userId);
            if (userId != null) {
                req.setAttribute(PARAM_USERNAME, user.getName());
                Cookie userIdCookie = new Cookie(COOKIE_USER_ID, userId);
                userIdCookie.setMaxAge(cookieLifeTime);
                resp.addCookie(userIdCookie);
                getServletContext().getRequestDispatcher("/homepage").forward(req, resp);
            } else {
                System.out.println("Incorrect password!");
                req.setAttribute(PARAM_ERR_MESS, "Incorrect login or password");
                getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
            }

        }
    }
}

