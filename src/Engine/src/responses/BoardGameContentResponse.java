package responses;

import Engine.BoardCell;
import java.awt.Point;
import java.util.List;

public class BoardGameContentResponse {
    private BoardCell[][] Board;
    private int Rows;
    private int Cols;
    private String Varient;
    private String ActiveGame;
    private String GameStatus;
    private String ComputerPlayer;
    private String Viewer;
    private List<Point> WinnersPoints;
    private String PlayerRetired;

    public BoardGameContentResponse(BoardCell[][] i_Board,int i_Rows, int i_Cols, String i_Varient, String i_ActiveGame, String i_GameStatus,
                                    String i_ComputerPlayer,List<Point> i_WinnersPoints, String i_Viewer, String i_PlayerRetired){
        Board = i_Board;
        Rows = i_Rows;
        Cols = i_Cols;
        Varient = i_Varient;
        ActiveGame = i_ActiveGame;
        GameStatus = i_GameStatus;
        ComputerPlayer = i_ComputerPlayer;
        WinnersPoints = i_WinnersPoints;
        Viewer = i_Viewer;
        PlayerRetired = i_PlayerRetired;
    }
}
