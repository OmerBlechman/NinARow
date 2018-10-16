package Game.Servlet;

import Game.Utils.SessionUtils;
import com.google.gson.Gson;
import constants.Constants;
import responses.StatisticsContentResponse;
import responses.UserDetailsContentResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UsersAuthServlet", urlPatterns = {"/users"})
public class UsersAuthServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String jsonResponse;
            jsonResponse = gson.toJson(new UserDetailsContentResponse(SessionUtils.getAttribute(request,Constants.USERNAME),SessionUtils.getAttribute(request,Constants.VIEWER)));
            out.println(jsonResponse);
            out.flush();
        }
    }
}
