package ver1.guiahorarios.progra1.FacebookConn;

import com.facebook.model.GraphUser;

/**
 * Created by sanchosv on 18/04/14.
 */
public class FacebookUser {

    private static FacebookUser _instance;

    private FacebookUser(){}

    public static FacebookUser getInstance()
    {
        if(_instance==null)
        {
            _instance = new FacebookUser();
        }
        return _instance;
    }

    private GraphUser user;

    public GraphUser getUser() {
        return user;
    }

    public void setUser(GraphUser user) {
        this.user = user;
    }
}
