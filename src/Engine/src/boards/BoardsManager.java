package boards;

import Engine.EngineGame;
import Engine.XMLParser;
import Game.Utils.SessionUtils;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.*;

//Board: gameName,createdUserName,amountOfRegistersPlayers,capacityOfPlayers,Engine,activeGame,

public class BoardsManager {
    private Map<String,Board> m_BoardsGame = new HashMap<>();

    public synchronized void addGame(InputStream i_GameDetails,String i_CreatedName) throws JAXBException {
        EngineGame newGame = new EngineGame(new XMLParser());
            newGame.loadGame(i_GameDetails,this);
            m_BoardsGame.put(newGame.getGameTitle(),new Board(newGame.getGameTitle(), i_CreatedName, newGame.getCapacityOfPlayers(),newGame,newGame.getStatus().name()));
    }

    public synchronized void removeGameName(String i_GameName) {
        m_BoardsGame.remove(i_GameName);
    }

    public synchronized List<Board> getBoards() {
        if(m_BoardsGame.size() != 0)
            return new ArrayList<Board>(m_BoardsGame.values());
        return null;
    }

    public synchronized Board getGameBoard(String i_GameName){
        return m_BoardsGame.get(i_GameName);
    }

    public synchronized boolean isUserExists(String i_GameName) {
        return m_BoardsGame.containsKey(i_GameName);
    }

}
