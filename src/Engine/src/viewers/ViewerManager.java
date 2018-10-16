package viewers;

import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and for that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class ViewerManager {

    private final Set<Viewer> viewersSet;

    public ViewerManager() {
        viewersSet = new HashSet<>();
    }

    public synchronized void addViewer(String username) {
        viewersSet.add(new Viewer(username));
    }

    public synchronized void removeViewer(String username) {
        Viewer toDelete = null;
        for(Viewer current:  viewersSet){
            if(current.getName().equals(username)){
                toDelete = current;
                break;
            }
        }
        if(toDelete != null)
            viewersSet.remove(toDelete);
    }

    public synchronized Set<Viewer> getViewers() {
        return viewersSet;
    }

    public synchronized boolean isViewerExists(String username) {
        for(Viewer current:  viewersSet){
            if(current.getName().equals(username)){
                return true;
            }
        }
        return false;
    }
}
