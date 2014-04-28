package ver1.guiahorarios.progra1.ConnectivityManager;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Observable;

import ver1.guiahorarios.progra1.Connectivity.CourseConn;
import ver1.guiahorarios.progra1.CourseOrganization.School;

/**
 * Created by sanchosv on 26/04/14.
 */
public class SchoolCoursesManager extends Observable{

    private static SchoolCoursesManager _instance;

    private SchoolCoursesManager(){}

    public static SchoolCoursesManager getInstance()
    {
        if(_instance==null)
        {
            _instance = new SchoolCoursesManager();
        }
        return _instance;
    }

    public ArrayList<School> schoolList = new ArrayList<School>();

    public void getSchoolCourses()
    {
        new AsyncGetAllCourses().execute();
    }

    class AsyncGetAllCourses extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                CourseConn.getInstance().postData();
                schoolList = CourseConn.getInstance().getSchoolList();
                //GroupConn.getInstance().getGroupsPerCourse();


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
