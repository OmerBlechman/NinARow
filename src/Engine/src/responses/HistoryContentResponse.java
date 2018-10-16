package responses;

import java.util.List;

public class HistoryContentResponse {
    private int index;
    private String GameActive;
    private List<String> HistoryMoves;

    public HistoryContentResponse(List<String> i_HistoryMoves, int i_Index,String i_Status){
        HistoryMoves = i_HistoryMoves;
        index = i_Index;
        GameActive = i_Status;
    }
}
