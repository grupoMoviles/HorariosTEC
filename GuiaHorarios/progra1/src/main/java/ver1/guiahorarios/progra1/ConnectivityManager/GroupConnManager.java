package ver1.guiahorarios.progra1.ConnectivityManager;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Observable;

import ver1.guiahorarios.progra1.Connectivity.GroupConn;
import ver1.guiahorarios.progra1.CourseOrganization.Group;

/**
 * Created by sanchosv on 26/04/14.
 */
public class GroupConnManager extends Observable{

    private static GroupConnManager _instance;

    private GroupConnManager(){}

    public static GroupConnManager getInstance()
    {
        if(_instance==null)
        {
            _instance = new GroupConnManager();
        }
        return _instance;
    }

    public void getGroupsPerCourse(String pCourse_code)
    {
        course_code = pCourse_code;
        new AsyncGetGroupPerCourse().execute();
    }

    public ArrayList<Group> group_list = new ArrayList<Group>();
    private String course_code;

    class AsyncGetGroupPerCourse extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                GroupConn.getInstance().getGroupsPerCourse(course_code);
                group_list = GroupConn.getInstance().getGroup_list();
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
