package by.bsu.famcs.drapegnik;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req.getParameter("name"), req.getParameter("pass"));
        String absolutePath = getServletContext().getRealPath("/");

        new UserHandler(absolutePath.substring(0, absolutePath.length() - 15) + "db.json");
        if (UserHandler.checkUser(user)) {
            req.setAttribute("username", user.getName());
            getServletContext().getRequestDispatcher("/homepage.jsp").forward(req, resp);
        } else {
            System.out.println("Incorrect password!");
            req.setAttribute("errorMessage", "Incorrect login or password");
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
