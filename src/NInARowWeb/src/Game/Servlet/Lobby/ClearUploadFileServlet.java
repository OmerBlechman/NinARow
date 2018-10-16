package Game.Servlet.Lobby;


import Game.Utils.SessionUtils;
import constants.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ClearUploadFileServlet", urlPatterns = {"/ClearUploadFile"})
public class ClearUploadFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionUtils.setAttribute(request,Constants.IS_UPDATE_FILE,null);
    }
}
