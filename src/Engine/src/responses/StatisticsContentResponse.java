package responses;

import Engine.BoardCell;
import Engine.generated.Game;
import viewers.Viewer;

import java.util.List;
import java.util.Set;

import java.util.List;

public class StatisticsContentResponse {
    private String PlayerNameTurn;
    private String Time;
    private String Target;
    private String Varient;
    private String NextPlayerName;
    private String GameActive;
    private final String ComputerTurn;
    private final String Winners;
    private final String WaitingMessage;
    private final String ClearError;

    public StatisticsContentResponse(String i_PlayerTurnName, String i_Time, String i_Target, String i_Varient, String i_NextPlayerName, String i_Status,
                                     String computerTurn, String winners, int capacityOfPlayers, int registeredPlayers, String i_ClearError){
        PlayerNameTurn = i_PlayerTurnName;
        Time = i_Time;
        Target = i_Target;
        Varient = i_Varient;
        NextPlayerName = i_NextPlayerName;
        GameActive = i_Status;
        ComputerTurn = computerTurn;
        Winners = winners;
        WaitingMessage = "For Starting game we need "  + capacityOfPlayers +" players \n We are waiting for another " +(capacityOfPlayers - registeredPlayers) +" players";
        ClearError = i_ClearError;
    }
}