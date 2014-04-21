package ver1.guiahorarios.progra1.UserInfo;

/**
 * Created by sanchosv on 18/04/14.
 */
public class UserChosen {

    private static UserChosen _instance;

    private UserChosen(){}

    public static UserChosen getInstance()
    {
        if(_instance==null)
        {
            _instance = new UserChosen();
        }
        return _instance;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
