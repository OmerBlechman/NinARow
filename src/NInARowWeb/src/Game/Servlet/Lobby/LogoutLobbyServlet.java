package Game.Servlet.Lobby;

import Game.Utils.ServletUtils;
import Game.Utils.SessionUtils;
import constants.Constants;
import users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogoutLobbyServlet", urlPatterns = {"/logoutLobby"})
public class LogoutLobbyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getAttribute(request,Constants.USERNAME);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession != null) {
            userManager.removeUser(usernameFromSession);
            SessionUtils.clearSession(request);
            response.sendRedirect(request.getContextPath());
        }
    }
}
