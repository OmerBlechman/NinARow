package Game.Servlet.GameRoom;

import Game.Utils.ServletUtils;
import Game.Utils.SessionUtils;
import boards.Board;
import boards.BoardsManager;
import com.google.gson.Gson;
import constants.Constants;
import responses.BoardGameContentResponse;
import responses.StatisticsContentResponse;
import viewers.Viewer;
import viewers.ViewerManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(name = "StatisticsServlet", urlPatterns = {"/Statistics"})
public class StatisticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String boardName = SessionUtils.getAttribute(request,Constants.BOARD_GAME);
        BoardsManager manager = ServletUtils.getBoardsManager(getServletContext());
        Board game = manager.getGameBoard(boardName);

        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String jsonResponse;
        String playerTurnError = SessionUtils.getAttribute(request,Constants.PLAYER_TURN_ERROR);
        String clearError = playerTurnError != null && Integer.toString(game.getCurrentPlayerUniqueID()).equals(SessionUtils.getAttribute(request,Constants.UNIQUE_ID))? "Yes" : null;
        if(game.getStatus() == "GAMING") {
            jsonResponse = gson.toJson(new StatisticsContentResponse(game.getPlayerName(game.getTurn()), game.getTime(), game.getTarget(), game.getVarient(), game.getPlayerName((game.getTurn() + 1) % game.getAmountOfRegistersPlayers()), game.getStatus(),
                    game.isComputerTurn() == true ? "Yes" : "No", game.getWinnersNames(), -1, -1, clearError));
            if(clearError != null)
                SessionUtils.removeAttribute(request,Constants.PLAYER_TURN_ERROR);
        }
        else
            jsonResponse = gson.toJson(new StatisticsContentResponse(null,null,game.getTarget(),game.getVarient(),null,game.getStatus(),null,game.getWinnersNames(),game.getCapacityOfPlayers(),game.getAmountOfRegistersPlayers(),null));
        out.print(jsonResponse);
    }
}
