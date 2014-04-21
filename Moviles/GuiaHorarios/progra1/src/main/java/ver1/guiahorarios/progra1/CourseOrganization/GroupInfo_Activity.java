package ver1.guiahorarios.progra1.CourseOrganization;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ver1.guiahorarios.progra1.Connectivity.CourseConn;
import ver1.guiahorarios.progra1.Connectivity.GroupConn;
import ver1.guiahorarios.progra1.Connectivity.Remover;
import ver1.guiahorarios.progra1.FacebookConn.Friends_Activity;
import ver1.guiahorarios.progra1.R;
import ver1.guiahorarios.progra1.Schedule.Schedule1;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

public class GroupInfo_Activity extends Activity {

    TextView tv_name;
    TextView tv_teacher;
    TextView tv_location;
    ListView list_view;
    ArrayAdapter<String> adapter;
    Button b_addGroup;
    Button b_face_friends;

    @Override
    public void onBackPressed() {
        Boolean wasOnUserCourses = getIntent().getExtras().getBoolean("lastActivity");
        if(wasOnUserCourses)
        {
            Intent i = new Intent(GroupInfo_Activity.this,UserCourse_Info.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(GroupInfo_Activity.this,AllCourseInfo_Activity.class);
            startActivity(i);
        }
    }

    public void getScheduleArray()
    {
        ArrayList<Group> grupos = new ArrayList<Group>(GroupsSelected.getInstance().getHash_groups().values());
        Log.e("AAAAAAAAA", "" + grupos.size());
        for(int i=0;i< grupos.size();i++)
        {
            Schedule1.getInstance().fillRange(grupos.get(i).getSchedule());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info_);
        tv_name = (TextView)findViewById(R.id.GIA_TV_Number);
        tv_teacher = (TextView)findViewById(R.id.GIA_TV_Teacher);
        tv_location = (TextView)findViewById(R.id.GIA_TV_Location);
        list_view = (ListView) findViewById(R.id.GIA_LV);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,GroupSelected.getInstance().getGroup().getSchedString());
        b_addGroup = (Button)findViewById(R.id.GIA_B_Add);
        b_face_friends = (Button)findViewById(R.id.GIA_B_FaceFriends);
        if(!UserChosen.getInstance().getUser().isHasFacebook())
        {
            b_face_friends.setEnabled(false);
        }
        list_view.setAdapter(adapter);
        setInfo();
        Schedule1.getInstance();
        Schedule1.getInstance().initialize();
        getScheduleArray();
        Log.e("GROUP INFO", CourseSelected.getInstance().getCourse().getCode());

    }

    public void viewFriends(View v)
    {
        Intent i = new Intent(GroupInfo_Activity.this, Friends_Activity.class);
        startActivity(i);
    }

    public void addGroup(View v)
    {
        Schedule1.getInstance().printCellsState();

        //Si ya tiene el curso seleccionado de ese grupo
        if(GroupsSelected.getInstance().getHash_groups().containsKey(CourseSelected.getInstance().getCourse().getCode()))
        {
            //Si el grupo ya lo tiene
            if(GroupsSelected.getInstance().getHash_groups().get(CourseSelected.getInstance().getCourse().getCode()).getId().equals(GroupSelected.getInstance().getGroup().getId()))
            {
                //CoursesSelected.getInstance().getHash_courses().remove(CourseSelected.getInstance().getCourse().getCode());
                //ArrayList<Course> courses =  new ArrayList<Course>(CoursesSelected.getInstance().getHash_courses().values());
                //CoursesSelected.getInstance().setCourse_list(courses);
                GroupsSelected.getInstance().getHash_groups().remove(CourseSelected.getInstance().getCourse().getCode());
                Remover.getInstance().removeGroup();
                //Eliminar
                b_addGroup.setText("Agregar");
            }
            //Si tiene otro grupo ya seleccionado
            else{

                if(Schedule1.getInstance().tryToFillRange(GroupSelected.getInstance().getGroup().getSchedule())){
                    if(GroupsSelected.getInstance().getHash_groups().containsKey(CourseSelected.getInstance().getCourse().getCode()))
                    {
                        //Eliminar el grupo anterior

                        Log.e("CURSO YA TIENE GRUPO","SI");

                        Remover.getInstance().removeGroup(GroupsSelected.getInstance().getHash_groups().get(CourseSelected.getInstance().getCourse().getCode()));
                        GroupsSelected.getInstance().getHash_groups().put(CourseSelected.getInstance().getCourse().getCode(),GroupSelected.getInstance().getGroup());
                        //Guardarlo en la base
                        if(UserChosen.getInstance().getUser().isHasFacebook())
                        {
                            new AsyncSaveFacebookUserGroup().execute();
                        }
                        else
                            new AsyncSaveGroup().execute();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Existe otro curso a la misma hora",Toast.LENGTH_SHORT).show();
                }
                }



        }
        //No tiene ningun grupo para ese curso
        else
        {

            if(Schedule1.getInstance().tryToFillRange(GroupSelected.getInstance().getGroup().getSchedule())){
                GroupsSelected.getInstance().getHash_groups().put(CourseSelected.getInstance().getCourse().getCode(),GroupSelected.getInstance().getGroup());
                //Ya tiene el curso pero no el grupo
                if(CoursesSelected.getInstance().getHash_courses().containsKey(CourseSelected.getInstance().getCourse().getCode()))
                {
                    GroupsSelected.getInstance().getHash_groups().put(CourseSelected.getInstance().getCourse().getCode(),GroupSelected.getInstance().getGroup());
                    if(UserChosen.getInstance().getUser().isHasFacebook())
                    {
                        new AsyncSaveFacebookUserGroup().execute();
                    }
                    else
                        new AsyncSaveGroup().execute();
                }
                //No tiene el grupo ni el curso
                else
                {
                    CoursesSelected.getInstance().getCourse_list().add(CourseSelected.getInstance().getCourse());
                    CoursesSelected.getInstance().getHash_courses().put(CourseSelected.getInstance().getCourse().getCode(),CourseSelected.getInstance().getCourse());
                    GroupsSelected.getInstance().getHash_groups().put(CourseSelected.getInstance().getCourse().getCode(),GroupSelected.getInstance().getGroup());
                    //Guardarlo en la base
                    if(UserChosen.getInstance().getUser().isHasFacebook())
                    {
                        new AsyncSaveFacebookUserCourse().execute();
                    }
                    else new AsyncSaveCourse().execute();
                }

         }
            else
            {
                Toast.makeText(getApplicationContext(),"Existe otro curso a la misma hora",Toast.LENGTH_SHORT).show();
            }
        }

        Schedule1.getInstance().printCellsState();
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
                GroupConn.getInstance().saveGroup(
                        GroupSelected.getInstance().getGroup().getNumber(), UserChosen.getInstance().getUser().getPassword(),
                        UserChosen.getInstance().getUser().getEmail(), CourseSelected.getInstance().getCourse().getCode());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            b_addGroup.setText("Eliminar");
        }
    }

    class AsyncSaveFacebookUserCourse extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
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
                GroupConn.getInstance().saveFacebookUserGroup(
                        GroupSelected.getInstance().getGroup().getNumber(),
                        UserChosen.getInstance().getUser().getFacebook(), CourseSelected.getInstance().getCourse().getCode());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            b_addGroup.setText("Eliminar");
        }
    }


    public void setInfo()
    {
        tv_name.setText(Integer.toString(GroupSelected.getInstance().getGroup().getNumber()));
        tv_teacher.setText(GroupSelected.getInstance().getGroup().getTeacher());
        tv_location.setText(GroupSelected.getInstance().getGroup().getLocation());
        //No se ha agregado nigun grupo al curso
        if(GroupsSelected.getInstance().getHash_groups().containsKey(CourseSelected.getInstance().getCourse().getCode()))
        {
            //Ya el grupo esta seleccionado
            if(GroupsSelected.getInstance().getHash_groups().get(CourseSelected.getInstance().getCourse().getCode()).getId().equals(GroupSelected.getInstance().getGroup().getId()))
            {
                b_addGroup.setText("Eliminar");
            }
            else b_addGroup.setText("Agregar");
        }
        else b_addGroup.setText("Agregar");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_info_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
