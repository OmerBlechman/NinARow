package Game.Servlet.GameRoom;

import Game.Utils.ServletUtils;
import Game.Utils.SessionUtils;
import boards.Board;
import boards.BoardsManager;
import com.google.gson.Gson;
import constants.Constants;
import responses.HistoryContentResponse;
import responses.StatisticsContentResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "HistoryServlet", urlPatterns = {"/History"})
public class HistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String boardName = SessionUtils.getAttribute(request, Constants.BOARD_GAME);
        BoardsManager manager = ServletUtils.getBoardsManager(getServletContext());
        Board game = manager.getGameBoard(boardName);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        int index = Integer.parseInt(request.getParameter("Index"));
        String jsonResponse = null;
        List<String> subListHistoryMove = createCustomHistory(index,game.getLastMoveIndex(),game);
        if(game.getStatus() != "PRE_GAME") {
            jsonResponse = gson.toJson(new HistoryContentResponse(subListHistoryMove, game.getLastMoveIndex(),game.getStatus()));
            //TODO: take care in retire details
            out.write(jsonResponse);
        }
    }

    private List<String> createCustomHistory(int i_Index,int i_HistoryItemsSize, Board game){
        List<String> subListHistoryMoves = new LinkedList<>();
        for(int i = i_Index;i< i_HistoryItemsSize;++i){
            subListHistoryMoves.add(game.getHistoryMove(i));
        }
        return subListHistoryMoves;
    }
}
