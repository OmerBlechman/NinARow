package Engine;

import Engine.generated.GameDescriptor;
import boards.BoardsManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;

public class XMLParser implements Parser {

    @Override
    public DetailsInput purse(InputStream i_InputStream, int i_MaxRows, int i_MinRows, int i_MaxCols, int i_MinCols, BoardsManager i_BoardsManager) throws JAXBException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            GameDescriptor gameDetails = (GameDescriptor) jaxbUnmarshaller.unmarshal(i_InputStream);
            return (checkAndGetDataToInitialGame(gameDetails, i_MaxRows, i_MinRows, i_MaxCols, i_MinCols, i_BoardsManager));
        } catch (JAXBException e) {
            throw new JAXBException("The file cannot be loaded");
        }
    }

    private DetailsInput checkAndGetDataToInitialGame(GameDescriptor i_GameDetails, int i_MaxRows, int i_MinRows, int i_MaxCols, int i_MinCols, BoardsManager i_BoardsManager) {
        int rows, cols, sequence, amountOfPlayers;
        VarientEnum variant = null;
        if (i_GameDetails.getGameType().equals("DynamicMultiPlayer") == false)
            throw new IllegalArgumentException(" The game type is not legal");
        if (i_GameDetails.getGame().getBoard() == null)
            throw new IllegalArgumentException("The board is not exist");
        rows = i_GameDetails.getGame().getBoard().getRows();
        if (rows > i_MaxRows || rows < i_MinRows) {
            throw new IllegalArgumentException("The rows in file is out of legal range");
        }
        cols = i_GameDetails.getGame().getBoard().getColumns().intValue();
        if (cols > i_MaxCols || cols < i_MinCols) {
            throw new IllegalArgumentException("The Columns in file is out of legal range");
        }
        sequence = i_GameDetails.getGame().getTarget().intValue();
        if (sequence >= rows)
            throw new IllegalArgumentException("The sequence is equal or smaller than rows");
        else if (sequence >= cols)
            throw new IllegalArgumentException("The sequence is equal or smaller than columns");
        else if (sequence <= 1)
            throw new IllegalArgumentException("The sequence is need to be bigger than 1");
        for (VarientEnum current : VarientEnum.values()) {
            if (current.toString().equals(i_GameDetails.getGame().getVariant()))
                variant = VarientEnum.valueOf(i_GameDetails.getGame().getVariant().toUpperCase());
        }
        if (variant == null)
            throw new IllegalArgumentException("The variant is not suitable");
        if(i_GameDetails.getDynamicPlayers() == null)
            throw new IllegalArgumentException("The DynamicPlayers field is not exist");
        if(i_GameDetails.getDynamicPlayers().getGameTitle() == "")
            throw new IllegalArgumentException("The DynamicPlayers field is not exist");
        if(i_BoardsManager.isUserExists(i_GameDetails.getDynamicPlayers().getGameTitle()))
            throw new IllegalArgumentException("The game name is exist");
        String gameName = i_GameDetails.getDynamicPlayers().getGameTitle();
        if (i_GameDetails.getDynamicPlayers().getTotalPlayers() < 2 || i_GameDetails.getDynamicPlayers().getTotalPlayers() > 6)
            throw new IllegalArgumentException("The capacity of players is not suitable");
        amountOfPlayers = i_GameDetails.getDynamicPlayers().getTotalPlayers();


      /*  if (i_GameDetails.getGameType() == "MultiPlayer") {
            if (i_GameDetails.getPlayers() == null || i_GameDetails.getPlayers().getPlayer() == null)
                throw new IllegalArgumentException("The number of players is not suitable to the amount of player field");
            if (i_GameDetails.getPlayers().getPlayer().size() < 2 || i_GameDetails.getPlayers().getPlayer().size() > 6)
                throw new IllegalArgumentException("The capacity of players is not suitable");
            List<Player> gamePlayers = i_GameDetails.getPlayers().getPlayer();
            amountOfPlayers = gamePlayers.size();
            List<PlayerInput> playerInputs = new ArrayList<PlayerInput>(amountOfPlayers);
            int computerCounters = 0;
            Set<Short> PlayersID = new HashSet<>();
            for (Player current : gamePlayers) {
                short currentPlayerId;
                String currentPlayerName;
                boolean isComputer;
                currentPlayerId = current.getId();
                currentPlayerName = current.getName();
                if (currentPlayerId < 0)
                    throw new IllegalArgumentException(currentPlayerName + " id is negative");
                else if (PlayersID.contains(currentPlayerId) == true)
                    throw new IllegalArgumentException("There are two players with the same ID");
                if (current.getType().equals(PlayerTypeEnum.COMPUTER.toString())) {
                    isComputer = true;
                    computerCounters++;
                } else if (current.getType().equals(PlayerTypeEnum.HUMAN.toString()))
                    isComputer = false;
                else
                    throw new IllegalArgumentException(current.getName() + " type is not legal");

                playerInputs.add(new PlayerInput(currentPlayerId, currentPlayerName, isComputer));
                PlayersID.add(currentPlayerId);
            }
            if (amountOfPlayers == computerCounters)
                throw new IllegalArgumentException(" The game must include at least a human player");
*/
        DetailsInput gameDetails = new DetailsInput();
        gameDetails.setRows(rows);
        gameDetails.setCols(cols);
        gameDetails.setSequence(sequence);
        gameDetails.setVariant(variant);
        gameDetails.setAmountOfPlayers(amountOfPlayers);
        gameDetails.setGameTitle(gameName);
        return gameDetails;
    }
}
