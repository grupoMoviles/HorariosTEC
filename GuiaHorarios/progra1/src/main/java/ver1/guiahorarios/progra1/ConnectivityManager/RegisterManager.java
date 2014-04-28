package ver1.guiahorarios.progra1.ConnectivityManager;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import ver1.guiahorarios.progra1.Connectivity.UserConn;
import ver1.guiahorarios.progra1.CourseOrganization.Course;
import ver1.guiahorarios.progra1.CourseOrganization.CoursesSelected;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupsSelected;

/**
 * Created by sanchosv on 26/04/14.
 */
public class RegisterManager extends Observable {

    private RegisterManager()
    {

    }

    public static RegisterManager getInstance()
    {
        if(_instance==null)
        {
            _instance = new RegisterManager();
        }
        return _instance;
    }

    public void registerUser(String pPassword,String pEmail)
    {
        password = pPassword;
        email = pEmail;
        new AsyncRegisterUser().execute();
    }

    class AsyncRegisterUser extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                UserConn.getInstance().postData(password,email);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed)
        {
            GroupsSelected.getInstance().setHash_groups(new HashMap<String, Group>());
            CoursesSelected.getInstance().setCourse_list(new ArrayList<Course>());
            CoursesSelected.getInstance().setHash_courses(new HashMap<String, Course>());
            setChanged();
            notifyObservers();
        }
    }

    private String email;
    private String password;
    private static RegisterManager _instance;


}
