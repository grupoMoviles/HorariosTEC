package ver1.guiahorarios.progra1.ConnectivityManager;

import android.os.AsyncTask;

import ver1.guiahorarios.progra1.Connectivity.RemoverConn;
import ver1.guiahorarios.progra1.CourseOrganization.CourseSelected;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupSelected;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

/**
 * Created by sanchosv on 19/04/14.
 */
public class RemoverManager {

    private static RemoverManager _instance;

    private RemoverManager(){}

    public static RemoverManager getInstance()
    {
        if(_instance==null)
        {
            _instance = new RemoverManager();
        }
        return _instance;
    }

    public Group group_to_remove;

    public void removeGroup()
    {
        if(UserChosen.getInstance().getUser().isHasFacebook())
        {
            new AsyncDeleteFacebookUserGroup().execute();
        }
        else new AsyncDeleteUserGroup().execute();
    }

    public void removeGroup(Group group)
    {
        group_to_remove = group;
        if(UserChosen.getInstance().getUser().isHasFacebook())
        {
            new AsyncDeleteFacebookUserGroup2().execute();
        }
        else new AsyncDeleteUserGroup2().execute();
    }

    public void removeCourse()
    {
        if(UserChosen.getInstance().getUser().isHasFacebook())
        {
            new AsyncDeleteFacebookUserCourse().execute();
        }
        else new AsyncDeleteUserCourse().execute();
    }

    class AsyncDeleteUserCourse extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                RemoverConn.getInstance().deleteCourse(UserChosen.getInstance().getUser().getPassword(),
                        UserChosen.getInstance().getUser().getEmail(), CourseSelected.getInstance().getCourse().getCode());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }

    class AsyncDeleteUserGroup extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                RemoverConn.getInstance().deleteGroup(UserChosen.getInstance().getUser().getPassword(),
                        UserChosen.getInstance().getUser().getEmail(), GroupSelected.getInstance().getGroup().getCourse_id(),
                        GroupSelected.getInstance().getGroup().getNumber());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }

    class AsyncDeleteUserGroup2 extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                Group group = group_to_remove;
                RemoverConn.getInstance().deleteGroup(UserChosen.getInstance().getUser().getPassword(),
                        UserChosen.getInstance().getUser().getEmail(), group.getCourse_id(),
                        group.getNumber());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }

    class AsyncDeleteFacebookUserCourse extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                RemoverConn.getInstance().deleteFacebookUserCourse(UserChosen.getInstance().getUser().getFacebook(), CourseSelected.getInstance().getCourse().getCode());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }

    class AsyncDeleteFacebookUserGroup extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                RemoverConn.getInstance().deleteFacebookUserGroup(UserChosen.getInstance().getUser().getFacebook(),
                        GroupSelected.getInstance().getGroup().getCourse_id(),
                        GroupSelected.getInstance().getGroup().getNumber());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }

    class AsyncDeleteFacebookUserGroup2 extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                RemoverConn.getInstance().deleteFacebookUserGroup(UserChosen.getInstance().getUser().getFacebook(),
                        group_to_remove.getCourse_id(),
                        group_to_remove.getNumber());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }
}
