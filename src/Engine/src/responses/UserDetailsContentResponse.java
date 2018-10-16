package responses;

import viewers.Viewer;

public class UserDetailsContentResponse {
    private String PlayerName;
    private String Viewer;

    public UserDetailsContentResponse(String i_PlayerName, String i_Viewer){
        PlayerName = i_PlayerName;
        Viewer = i_Viewer;
    }
}
