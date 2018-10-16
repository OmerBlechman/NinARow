package Engine;

import boards.BoardsManager;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public interface CommandsInterface {
    void startGame();

    DetailsInput loadGame(InputStream i_GameDetails, BoardsManager i_BoardsManager) throws JAXBException;

    List<DataHistoryDisc> getHistory();

    void undo();

    void restartGame();

    void saveGame(ObjectOutputStream i_DataOut) throws IOException;
}
