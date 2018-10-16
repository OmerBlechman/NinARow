package Engine;

import boards.BoardsManager;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.Serializable;

public interface Parser extends Serializable {
    DetailsInput purse(InputStream i_InputStream, int i_MaxRows, int i_MinRows , int i_MaxCols, int i_MinCols, BoardsManager i_BoardsManager) throws JAXBException;
}
