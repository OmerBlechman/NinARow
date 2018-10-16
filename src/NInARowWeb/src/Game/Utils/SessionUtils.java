package Game.Utils;
import constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static constants.Constants.USERNAME;

public class SessionUtils {
    public static String getAttribute(HttpServletRequest request,String i_Attribute) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(i_Attribute) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static void setAttribute(HttpServletRequest request,String i_Attribute, String i_Value)
    {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session.getAttribute(i_Attribute);
        if(sessionAttribute != null)
            session.removeAttribute(i_Attribute);
        session.setAttribute(i_Attribute,i_Value);
    }

    public static void removeAttribute(HttpServletRequest request,String i_Attribute){
        HttpSession session = request.getSession(false);
        session.removeAttribute(i_Attribute);
    }
}