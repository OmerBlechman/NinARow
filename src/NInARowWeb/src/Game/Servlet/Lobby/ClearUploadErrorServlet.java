package Game.Servlet.Lobby;

import Game.Utils.SessionUtils;
import constants.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ClearUploadErrorServlet", urlPatterns = {"/ClearUploadError"})
public class ClearUploadErrorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionUtils.setAttribute(request,Constants.ERROR_UPLOAD_GAME_ERROR,null);
    }
}
