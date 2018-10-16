package users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and for that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<User> usersSet;

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(String username) {
        usersSet.add(new User(username));
    }

    public synchronized void removeUser(String username) {
        User toDelete = null;
        for(User current:  usersSet){
            if(current.getName().equals(username)){
                toDelete = current;
                break;
            }
        }
        if(toDelete != null)
            usersSet.remove(toDelete);
    }

    public synchronized Set<User> getUsers() {
        return usersSet;
    }

    public synchronized boolean isUserExists(String username) {
        for(User current:  usersSet){
            if(current.getName().equals(username)){
                return true;
            }
        }
        return false;
    }
}
