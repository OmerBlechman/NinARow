package users;

public class User {
    private String m_Name;
    private boolean m_Computer;
    public User(String i_Name){
        m_Name = i_Name;
    }

    public String getName() {
        return m_Name;
    }
}
