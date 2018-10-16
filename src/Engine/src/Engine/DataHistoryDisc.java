package Engine;

import java.awt.*;
import java.io.Serializable;

public class DataHistoryDisc implements Serializable {
    private String m_Name;
    private Point m_LastMoveCoordinate;
    private char m_Sign;
    private boolean m_Popout;
    private boolean m_RetiredMove;

    public DataHistoryDisc(String i_Name, Point i_LastMoveCoordinate, char i_Sign, boolean i_Popout, boolean i_Retired){
        m_Name = i_Name;
        m_LastMoveCoordinate = i_LastMoveCoordinate;
        m_Sign = i_Sign;
        m_Popout = i_Popout;
        m_RetiredMove = i_Retired;

    }

    public String getName() {
        return m_Name;
    }

    public boolean getPopout() {
        return m_Popout;
    }

    public Point getLastMoveCoordinate() {
        return m_LastMoveCoordinate;
    }

    public char getSign() {
        return m_Sign;
    }

    public boolean getRetiredMove(){
        return m_RetiredMove;
    }
}
