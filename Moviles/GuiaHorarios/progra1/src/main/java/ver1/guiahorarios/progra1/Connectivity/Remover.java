package ver1.guiahorarios.progra1.Connectivity;

import android.os.AsyncTask;
import android.util.Log;

import ver1.guiahorarios.progra1.CourseOrganization.CourseSelected;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupSelected;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

/**
 * Created by sanchosv on 19/04/14.
 */
public class Remover {

    private static Remover _instance;

    private Remover(){}

    public static Remover getInstance()
    {
        if(_instance==null)
        {
            _instance = new Remover();
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
        new AsyncDeleteUserGroup().execute();
    }

    public void removeGroup(Group group)
    {
        group_to_remove = group;
        if(UserChosen.getInstance().getUser().isHasFacebook())
        {
            new AsyncDeleteFacebookUserGroup2().execute();
        }
        new AsyncDeleteUserGroup2().execute();
    }

    public void removeCourse()
    {
        if(UserChosen.getInstance().getUser().isHasFacebook())
        {
            new AsyncDeleteFacebookUserCourse().execute();
        }
        new AsyncDeleteUserCourse().execute();
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

                Log.e("CODIGO REMOVER", CourseSelected.getInstance().getCourse().getCode());
                Log.e("NUMERO REMOVER", Integer.toString(GroupSelected.getInstance().getGroup().getNumber()));

                RemoverConn.getInstance().deleteGroup(UserChosen.getInstance().getUser().getPassword(),
                        UserChosen.getInstance().getUser().getEmail(), CourseSelected.getInstance().getCourse().getCode(),
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

                Log.e("CODIGO REMOVER", CourseSelected.getInstance().getCourse().getCode());
                Log.e("NUMERO REMOVER", Integer.toString(GroupSelected.getInstance().getGroup().getNumber()));

                RemoverConn.getInstance().deleteGroup(UserChosen.getInstance().getUser().getPassword(),
                        UserChosen.getInstance().getUser().getEmail(), CourseSelected.getInstance().getCourse().getCode(),
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
                        CourseSelected.getInstance().getCourse().getCode(),
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
                        CourseSelected.getInstance().getCourse().getCode(),
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
