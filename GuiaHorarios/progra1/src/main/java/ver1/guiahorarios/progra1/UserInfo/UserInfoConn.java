package ver1.guiahorarios.progra1.UserInfo;

import android.os.AsyncTask;

import java.util.ArrayList;

import ver1.guiahorarios.progra1.Connectivity.CourseConn;
import ver1.guiahorarios.progra1.Connectivity.GroupConn;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupsSelected;
import ver1.guiahorarios.progra1.Schedule.Schedule1;

/**
 * Created by sanchosv on 19/04/14.
 */
public class UserInfoConn {

    private static UserInfoConn _instance;

    private UserInfoConn(){}

    public static UserInfoConn getInstance()
    {
        if(_instance==null)
        {
            _instance = new UserInfoConn();
        }
        return _instance;
    }

    public void getUserCourses()
    {
        new AsyncGetUserCourses().execute();
    }

    class AsyncGetUserCourses extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                CourseConn.getInstance().getUserCourses(UserChosen.getInstance().getUser().getEmail(),UserChosen.getInstance().getUser().getPassword());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }

    public void getFacebookUserCourses()
    {
        new AsyncGetUserFacebookCourses().execute();
    }

    class AsyncGetUserFacebookCourses extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                CourseConn.getInstance().getFacebookUserCourses(UserChosen.getInstance().getUser().getFacebook());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }

    public void getFacebookUserGroups()
    {
        new AsyncGetUserFacebookGroups().execute();
    }

    class AsyncGetUserFacebookGroups extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                GroupConn.getInstance().getFacebookUserGroups(UserChosen.getInstance().getUser().getFacebook());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            //Ya tiene grupos de facebook
            Schedule1.getInstance();
            Schedule1.getInstance().initialize();
            getScheduleArray();
        }
    }

    public void getUserGroups()
    {
        new AsyncGetUserGroups().execute();
    }

    class AsyncGetUserGroups extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                GroupConn.getInstance().getUserGroups(UserChosen.getInstance().getUser().getEmail(),UserChosen.getInstance().getUser().getPassword());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            //Ya tiene grupos de usuario
            Schedule1.getInstance();
            Schedule1.getInstance().initialize();
            getScheduleArray();
        }
    }

    public void getScheduleArray()
    {
        ArrayList<Group> grupos = new ArrayList<Group>(GroupsSelected.getInstance().getHash_groups().values());
        for(int i=0;i< grupos.size();i++)
        {
           Schedule1.getInstance().fillRange(grupos.get(i).getSchedule());
        }
    }

}
