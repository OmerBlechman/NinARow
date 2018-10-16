package Game.Utils;


import boards.Board;
import boards.BoardsManager;
import chat.ChatManager;
import constants.Constants;
import users.UserManager;
import viewers.ViewerManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
//import static chat.Constants.INT_PARAMETER_ERROR;
public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String BOARDS_MANAGER_ATTRIBUTE_NAME = "boardManager";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained unchronicled for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object boardsManagerLock = new Object();
	private static final Object chatManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static BoardsManager getBoardsManager(ServletContext servletContext) {

		synchronized (boardsManagerLock) {
			if (servletContext.getAttribute(BOARDS_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(BOARDS_MANAGER_ATTRIBUTE_NAME, new BoardsManager());
			}
		}
		return (BoardsManager) servletContext.getAttribute(BOARDS_MANAGER_ATTRIBUTE_NAME);
	}

	public static ChatManager getChatManager(Board gameBoard) {
		synchronized (chatManagerLock) {
			return gameBoard.getChatManager();
		}
	}

	public static ViewerManager getViewerManager(Board gameBoard) {
		synchronized (chatManagerLock) {
			return gameBoard.getViewerManager();
		}
	}


	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return Constants.INT_PARAMETER_ERROR;
	}
}
