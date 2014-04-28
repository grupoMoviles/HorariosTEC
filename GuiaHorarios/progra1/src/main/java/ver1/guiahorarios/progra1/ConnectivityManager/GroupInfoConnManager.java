package ver1.guiahorarios.progra1.ConnectivityManager;

import android.os.AsyncTask;

import java.util.Observable;

import ver1.guiahorarios.progra1.Connectivity.CourseConn;
import ver1.guiahorarios.progra1.Connectivity.GroupConn;
import ver1.guiahorarios.progra1.CourseOrganization.CourseSelected;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupSelected;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

/**
 * Created by sanchosv on 26/04/14.
 */
public class GroupInfoConnManager extends Observable {

    private static GroupInfoConnManager _instance;

    private GroupInfoConnManager(){}

    public static GroupInfoConnManager getInstance()
    {
        if(_instance==null)
        {
            _instance = new GroupInfoConnManager();
        }
        return _instance;
    }

    public void saveUserGroup()
    {
        new AsyncSaveGroup().execute();

    }

    public void saveUserCourse()
    {
        new AsyncSaveCourse().execute();
    }

    public void saveFacebookUserGroup()
    {
        new AsyncSaveFacebookUserGroup().execute();
    }

    public void saveFacebookUserCourse()
    {
        new AsyncSaveFacebookUserCourse().execute();
    }
    class AsyncSaveCourse extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                CourseConn.getInstance().saveCourse(CourseSelected.getInstance().getCourse(), UserChosen.getInstance().getUser().getPassword(), UserChosen.getInstance().getUser().getEmail());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            new AsyncSaveGroup().execute();
        }
    }

    class AsyncSaveGroup extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                Group group = new Group();
                group = GroupSelected.getInstance().getGroup();
                GroupConn.getInstance().saveGroup(
                        group.getNumber(), UserChosen.getInstance().getUser().getPassword(),
                        UserChosen.getInstance().getUser().getEmail(), group.getCourse_id());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            setChanged();
            notifyObservers();
        }
    }

    class AsyncSaveFacebookUserCourse extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                Group group = new Group();
                group = GroupSelected.getInstance().getGroup();
                CourseConn.getInstance().saveFacebookUSerCourse(CourseSelected.getInstance().getCourse(), UserChosen.getInstance().getUser().getFacebook());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            new AsyncSaveFacebookUserGroup().execute();
        }
    }

    class AsyncSaveFacebookUserGroup extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                Group group = new Group();
                group = GroupSelected.getInstance().getGroup();
                GroupConn.getInstance().saveFacebookUserGroup(
                        group.getNumber(),
                        UserChosen.getInstance().getUser().getFacebook(), group.getCourse_id());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
           setChanged();
           notifyObservers();
        }
    }
}
