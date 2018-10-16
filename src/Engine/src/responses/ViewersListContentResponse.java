package responses;

import viewers.Viewer;

import java.util.Set;

public class ViewersListContentResponse {
    private Set<Viewer> Viewers;

    public ViewersListContentResponse(Set<Viewer> i_Viewers){
        Viewers = i_Viewers;
    }
}
