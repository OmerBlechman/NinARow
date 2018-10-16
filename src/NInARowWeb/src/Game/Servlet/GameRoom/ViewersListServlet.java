package Game.Servlet.GameRoom;

import Game.Utils.ServletUtils;
import Game.Utils.SessionUtils;
import boards.Board;
import boards.BoardsManager;
import com.google.gson.Gson;
import constants.Constants;
import responses.BoardGameContentResponse;
import responses.ViewersListContentResponse;
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


@WebServlet(name = "ViewersListServlet", urlPatterns = {"/viewers"})
public class ViewersListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String boardName = SessionUtils.getAttribute(request,Constants.BOARD_GAME);
        BoardsManager manager = ServletUtils.getBoardsManager(getServletContext());
        Board game = manager.getGameBoard(boardName);
        ViewerManager viewerManager = game.getViewerManager();
        Set<Viewer> viewers = viewerManager.getViewers();
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new ViewersListContentResponse(viewers));
        out.print(jsonResponse);
    }
}
