package boards;

import Engine.*;
import chat.ChatManager;
import constants.ColorOnBoardEnum;
import viewers.ViewerManager;

import javax.swing.text.View;
import java.awt.Point;
import java.util.List;

public class Board {
    private final String GameName;
    private final String CreatedUserName;
    private int RegisteredPlayers;
    private int HumanPlayers;
    private String RegisteredPlayersToResponse;
    private int ViewersAmount;
    private final int CapacityOfPlayers;
    private final EngineGame Engine;
    private String ActiveGame;
    private int Target;
    private String BoardSize;
    private String Status;
    private ChatManager Chat;
    private ViewerManager Viewer;
    private String PlayerRetired = null;

    public Board(String i_GameName, String i_CreatedUserName, int i_CapacityOfPlayers, EngineGame i_Engine, String i_Status){
        GameName = i_GameName;
        CreatedUserName = i_CreatedUserName;
        CapacityOfPlayers = i_CapacityOfPlayers;
        RegisteredPlayers = 0;
        updateRegisteredPlayerResponse();
        Engine = i_Engine;
        ActiveGame = "No";
        Target = Engine.getSequence();
        BoardSize = Engine.getRows() + "X" + Engine.getMaxCol();
        Status = i_Status;
        HumanPlayers = 0;
        Chat = new ChatManager();
        Viewer = new ViewerManager();
        ViewersAmount = 0;
    }

    public void setPlayerRetired(String i_Message){
        PlayerRetired = i_Message;
    }

    public String getPlayerRetired(){
        return PlayerRetired;
    }

    public void startGame(){
        Engine.startGame();
    }

    public String getTime(){
        final int secondsInMinutes = 60;
        long seconds = Engine.getTimeInSeconds();
        return(String.format(" %02d:%02d", (seconds / secondsInMinutes), (seconds % secondsInMinutes)));
    }

    public int getLastMoveIndex(){return  Engine.getAmountOfMoves();}

    public String getStatus(){
        return Status;
    }

    public void updateStatus(){
        Status = Engine.getStatus().name();
    }

    public int getAmountOfRegistersPlayers() {
        return Engine.getAmountOfPlayers();
    }
    
    public boolean isActiveGame() {
        return ActiveGame == "Yes";
    }

    public void setActiveGame(boolean i_ActiveGame) {
        this.ActiveGame = i_ActiveGame == true? "Yes" : "No";
    }
    
    private SignOnBoardEnum purseSign(char i_Sign){
        for(SignOnBoardEnum currentSign : SignOnBoardEnum.values()){
            if(currentSign.getSign() == i_Sign)
                return currentSign;
        }
        return null;
    }

    public String getHistoryMove(int i_Index){
        if(Engine.getAmountOfMoves() > 0) {
            DataHistoryDisc lastMove = Engine.getMoveByIndex(i_Index);
            if (lastMove.getRetiredMove() == false) {
                SignOnBoardEnum currentSign = purseSign(lastMove.getSign());
                return " " + lastMove.getName() + ", Color: " + ColorOnBoardEnum.valueOf((currentSign.name())).getColor() +
                        ", " + (lastMove.getPopout() ? "Popout: " : "Insert: ") + "(" + (lastMove.getLastMoveCoordinate().y + 1) + "," + (lastMove.getLastMoveCoordinate().x + 1) + ")";
            }
            else{
                return " " + lastMove.getName() + ", Retired!" ;
            }
        }
        return null;
    }

    public BoardCell[][] getBoard(){
        return Engine.getBoardForDisplay();
    }

    public int getRows(){
        return Engine.getRows();
    }

    public int getCols(){
        return Engine.getMaxCol();
    }

    public String getVarient(){
        return Engine.getVarient().name();
    }

    public String getTarget(){
        return Integer.toString(Engine.getSequence());
    }

    public int getTurn(){return Engine.getTurn();}

    public String getPlayerName(int i_Index){
        return Engine.getPlayerName(i_Index);
    }

    public String getPlayerType(int i_Index){return Engine.getPlayerType(i_Index)? "Computer" : "Human Player";}

    public String getPlayerTurns(int i_Index){return Integer.toString(Engine.getPlayerTurns(i_Index));}

    public String getColor(int i_Index)
    {
        char signOnBoard = Engine.getPlayerSignOnBoard(i_Index);
        for(SignOnBoardEnum currentSign: SignOnBoardEnum.values()){
            if(currentSign.getSign() == signOnBoard)
                return currentSign.name();
        }
        return null;
    }

    public List<Point> getWinnersCoordinates(){
        return Engine.getWinnersTarget();
    }

    public int getRegisteredPlayers(){
        return RegisteredPlayers;
    }

    public int getCurrentPlayerUniqueID(){
        return Engine.getUniqueID();
    }

    public ChatManager getChatManager(){
        return Chat;
    }

    public ViewerManager getViewerManager(){
        return Viewer;
    }

    public synchronized void addViewer(String i_RegisterPlayerName){
        Viewer.addViewer(i_RegisterPlayerName);
        ViewersAmount++;
    }

    public synchronized void removeViewer(String i_RegisterPlayerName){
        Viewer.removeViewer(i_RegisterPlayerName);
        ViewersAmount--;
    }

    public synchronized boolean addPlayer(String i_RegisterPlayerName, boolean i_IsComputerPlayer) {
        if(i_IsComputerPlayer && HumanPlayers == 0 && (1 + RegisteredPlayers == CapacityOfPlayers) ||
                RegisteredPlayers == CapacityOfPlayers)
            return false;
        if(!i_IsComputerPlayer)
            HumanPlayers++;
        Engine.addPlayer(i_RegisterPlayerName,i_IsComputerPlayer,RegisteredPlayers);
        RegisteredPlayers++;
        updateRegisteredPlayerResponse();
        if(RegisteredPlayers == CapacityOfPlayers){
            ActiveGame = "Yes";
        }
        return true;
    }

    private synchronized void updateRegisteredPlayerResponse() {
        RegisteredPlayersToResponse = RegisteredPlayers + "/" + CapacityOfPlayers;
    }

    public int getCapacityOfPlayers(){
        return CapacityOfPlayers;
    }

    public Point ComputerMove() {
        Point currentMove = null;
        if (Engine.ComputerTurn(Engine.getTurn())) {
            currentMove = Engine.computerOperation();
            finishedTurn(currentMove);
        }
        return currentMove;
    }

    private void finishedTurn(Point move){
        if(move != null) {
            Engine.finishedTurn(move);
            if(Engine.checkFinishedGame()){
                updateStatus();
            }
        }
    }

    public Point playerMove(int i_Col, boolean i_Popout){
        Point move = Engine.humanMove(i_Col,i_Popout);
        finishedTurn(move);
        return move;
    }

    private void restartGame(){
        ActiveGame = "No";
        Engine.restartGame();
        Chat = new ChatManager();
        PlayerRetired = null;
    }

    public synchronized void quitGame(int i_UniqueID,boolean computerPlayer){
        if(Status == "GAMING")
            Engine.quitGame(i_UniqueID);
        else if(Status == "PRE_GAME")
            Engine.removePlayerByUniqueID(i_UniqueID);
        RegisteredPlayers--;
        updateRegisteredPlayerResponse();
        if(!computerPlayer)
            HumanPlayers--;
        if(RegisteredPlayers == 0){
            restartGame();
        }
        if(RegisteredPlayers <= 1){
            updateStatus();
        }
    }

    public boolean isComputerTurn(){
        return Engine.ComputerTurn(Engine.getTurn());
    }

    public String getWinnersNames() {
        StringBuilder endGameMessage = new StringBuilder();
        if(Engine.getWinner().size() != 0 || Engine.getDrawMode()) {

            if (Engine.getWinner().size() == 1) {
                endGameMessage.append(Engine.getWinner().get(0).getName() + " Won!");
            } else if (Engine.getWinner().size() >= 2) {
                endGameMessage.append("The winners are: ");
                for (PlayerEngine currentPlayer : Engine.getWinner()) {
                    endGameMessage.append(currentPlayer.getName() + ", ");
                }
                endGameMessage.substring(0, endGameMessage.length() - 2);
            } else
                endGameMessage.append("Draw! The board is full");
            return endGameMessage.toString();
        }
        return null;
    }
}
