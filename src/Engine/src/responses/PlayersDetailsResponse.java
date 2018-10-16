package responses;

import java.util.LinkedList;
import java.util.List;

public class PlayersDetailsResponse {
    private List<PlayerDetails> Players = new LinkedList();
    private String GameActive = null;

    public PlayersDetailsResponse(String i_Status){
        GameActive = i_Status;
    }

    public void addPlayer(PlayerDetails newPlayer){
        Players.add(newPlayer);
    }

    public void removePlayer(PlayerDetails newPlayer){
        Players.remove(newPlayer);
    }
}
