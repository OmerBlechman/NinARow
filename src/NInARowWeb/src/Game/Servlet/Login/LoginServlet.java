package Game.Servlet.Login;

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
import java.io.PrintWriter;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.print(SessionUtils.getAttribute(request,Constants.ERROR_LOGIN_MESSAGE));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String usernameFromSession = SessionUtils.getAttribute(request,Constants.USERNAME);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        PrintWriter out = response.getWriter();
        if (usernameFromSession == null) {
            //user is not logged in yet
            String usernameFromParameter = request.getParameter(Constants.USERNAME);
            if (usernameFromParameter == null || usernameFromParameter == "") {
                request.getSession(true).setAttribute(Constants.ERROR_LOGIN_MESSAGE,"You need to insert a name");
                response.sendRedirect(request.getContextPath());
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        // username already exists, forward the request back to index.jsp
                        // with a parameter that indicates that an error should be displayed
                        // the request dispatcher obtained from the servlet context is one that MUST get an absolute path (starting with'/')
                        // and is relative to the web app root
                        // see this link for more details:
                        // http://timjansen.github.io/jarfiller/guide/servlet25/requestdispatcher.xhtml
                        request.getSession(true).setAttribute(Constants.ERROR_LOGIN_MESSAGE,errorMessage);
                        //out.print(errorMessage);
                        //response.sendError(403,errorMessage);
                        response.sendRedirect(request.getContextPath());
                    } else {
                         String computer = "false";
                        if(request.getParameter(Constants.TYPE) != null)
                            computer = "true";
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter);
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                        request.getSession(true).setAttribute(Constants.TYPE, computer);
                        SessionUtils.setAttribute(request,Constants.ERROR_LOGIN_MESSAGE,null);
                        //redirect the request to the chat room - in order to actually change the URL
                        response.sendRedirect(Constants.LOBBY_ROOM_URL);
                    }
                }
            }
        } else {
            response.sendRedirect(Constants.LOBBY_ROOM_URL);
            //user is already logged in
//            out.print(Constants.LOBBY_ROOM_URL);
//            response.sendRedirect(Constants.LOBBY_ROOM_URL);
        }
    }
}
