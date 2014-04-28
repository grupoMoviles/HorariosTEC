package ver1.guiahorarios.progra1.ConnectivityManager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Observable;

import ver1.guiahorarios.progra1.Connectivity.UserConn;
import ver1.guiahorarios.progra1.FacebookConn.FacebookUser;
import ver1.guiahorarios.progra1.UserInfo.UserInfoConn;

/**
 * Created by sanchosv on 26/04/14.
 */
public class LogInManager extends Observable {

    private static LogInManager _instance;

    private LogInManager()
    {

    }

    public static LogInManager getInstance()
    {
        if(_instance==null)
        {
            _instance = new LogInManager();
        }
        return _instance;
    }

    private Context context;
    private String email;
    private String password;

    public void logInFacebookUser(Context pContext)
    {
        context = pContext;
        new AsyncFacebookLog().execute();
    }

    public void logInCustomUser(Context pContext,String pEmail,String pPassword)
    {
        context = pContext;
        email = pEmail;
        password = pPassword;
        new AsyncLogInCustomUser().execute();
    }

    class AsyncFacebookLog extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                Log.e("FACEBOOK ID", FacebookUser.getInstance().getUser().getId());
                UserConn.getInstance().logInFacebook(FacebookUser.getInstance().getUser().getId());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            if(UserConn.logInSuccess)
            {
                //UserInfoConn.getInstance().getUserCourses();
                UserInfoConn.getInstance().getFacebookUserCourses();
                UserInfoConn.getInstance().getFacebookUserGroups();
                //
                setChanged();
                notifyObservers("YES");
            }
            else
            {
                setChanged();
                notifyObservers("NO");
                Toast.makeText(context, "Revise sus datos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AsyncLogInCustomUser extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                UserConn.getInstance().logIn(password, email);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            if(UserConn.logInSuccess)
            {
                UserInfoConn.getInstance().getUserCourses();
                UserInfoConn.getInstance().getUserGroups();

                setChanged();
                notifyObservers("YES");
            }
            else
            {
                setChanged();
                notifyObservers("NO");
                Toast.makeText(context,"Revise sus datos",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
