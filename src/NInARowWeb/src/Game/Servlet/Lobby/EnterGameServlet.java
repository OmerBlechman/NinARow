package Game.Servlet.Lobby;

import Game.Utils.ServletUtils;
import Game.Utils.SessionUtils;
import boards.Board;
import boards.BoardsManager;
import com.google.gson.Gson;
import constants.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "EnterGameServlet", urlPatterns = {"/EnterGame"})
public class EnterGameServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BoardsManager manager = ServletUtils.getBoardsManager(getServletContext());
        Board game = manager.getGameBoard(request.getParameter(Constants.GAME_NAME));
        String registerPlayerName = SessionUtils.getAttribute(request,Constants.USERNAME);
        boolean isComputerPlayer = SessionUtils.getAttribute(request,Constants.TYPE) == "true";
        SessionUtils.setAttribute(request,Constants.UNIQUE_ID,Integer.toString(game.getRegisteredPlayers()));
        boolean successUpload = game.addPlayer(registerPlayerName,isComputerPlayer);
        if(successUpload)
            SessionUtils.setAttribute(request,Constants.BOARD_GAME,request.getParameter(Constants.GAME_NAME));
        else
        {
            try(PrintWriter out = response.getWriter()){
                out.println("You cannot enter to this game");
                out.flush();
            }
        }
    }
}
