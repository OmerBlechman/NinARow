package Game.Servlet.GameRoom;

import Game.Utils.ServletUtils;
import Game.Utils.SessionUtils;
import boards.Board;
import boards.BoardsManager;
import com.google.gson.Gson;
import constants.ColorOnBoardEnum;
import constants.Constants;
import responses.PlayerDetails;
import responses.PlayersDetailsResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "PlayersDetailsServlet", urlPatterns = {"/PlayersDetails"})
public class PlayersDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String boardName = SessionUtils.getAttribute(request, Constants.BOARD_GAME);
        BoardsManager manager = ServletUtils.getBoardsManager(getServletContext());
        Board game = manager.getGameBoard(boardName);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        if (game.isActiveGame()) {
            PlayersDetailsResponse players = new PlayersDetailsResponse(game.getStatus());
            for(int i = 0;i<game.getAmountOfRegistersPlayers();++i){
                players.addPlayer(new PlayerDetails(game.getPlayerName(i),game.getPlayerType(i),ColorOnBoardEnum.valueOf(game.getColor(i)).getColor(),game.getPlayerTurns(i)));
            }
            String jsonResponse = gson.toJson(players);
            out.print(jsonResponse);
        }
    }
}
