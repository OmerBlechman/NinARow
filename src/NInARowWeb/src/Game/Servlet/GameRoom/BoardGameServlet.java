package Game.Servlet.GameRoom;

import Game.Utils.ServletUtils;
import Game.Utils.SessionUtils;
import boards.Board;
import boards.BoardsManager;
import com.google.gson.Gson;
import constants.Constants;
import responses.BoardGameContentResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "BoardGameServlet", urlPatterns = {"/BoardGame"})
public class BoardGameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String boardName = SessionUtils.getAttribute(request,Constants.BOARD_GAME);
        BoardsManager manager = ServletUtils.getBoardsManager(getServletContext());
        Board game = manager.getGameBoard(boardName);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new BoardGameContentResponse(game.getBoard(),game.getRows(),game.getCols(),game.getVarient(),game.isActiveGame()? "Yes" : "No",game.getStatus(),
                SessionUtils.getAttribute(request,Constants.TYPE),game.getWinnersCoordinates(),SessionUtils.getAttribute(request,Constants.VIEWER), game.getPlayerRetired()));
        out.print(jsonResponse);
    }
}
