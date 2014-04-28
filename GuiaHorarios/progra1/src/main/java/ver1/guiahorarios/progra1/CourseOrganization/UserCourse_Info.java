package ver1.guiahorarios.progra1.CourseOrganization;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ver1.guiahorarios.progra1.Connectivity.InternetConnection;
import ver1.guiahorarios.progra1.ConnectivityManager.RemoverManager;
import ver1.guiahorarios.progra1.R;

public class UserCourse_Info extends Activity {

    TextView tv_course_name;
    TextView tv_credits;
    TextView tv_code;
    TextView tv_school;
    Button b_addgroup;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_course__info);
        tv_code = (TextView)findViewById(R.id.UCIA_TV_Code);
        tv_course_name = (TextView)findViewById(R.id.UCIA_TV_Name);
        tv_credits = (TextView)findViewById(R.id.UCIA_TV_Credits);
        tv_school = (TextView)findViewById(R.id.UCIA_TV_School);
        b_addgroup = (Button)findViewById(R.id.UCIA_B_AddGroup);
        setInfo();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UserCourse_Info.this,UserCourses_Activity.class);
        startActivity(i);
    }

    public void removeCourseAccept()
    {
        CoursesSelected.getInstance().getHash_courses().remove(CourseSelected.getInstance().getCourse().getCode());
        ArrayList<Course> courses =  new ArrayList<Course>(CoursesSelected.getInstance().getHash_courses().values());
        CoursesSelected.getInstance().setCourse_list(courses);
        if(GroupsSelected.getInstance().getHash_groups().containsKey(CourseSelected.getInstance().getCourse().getCode()));
        {
            GroupSelected.getInstance().setGroup(GroupsSelected.getInstance().getHash_groups().get(course.getCode()));
            GroupsSelected.getInstance().getHash_groups().remove(CourseSelected.getInstance().getCourse().getCode());
            RemoverManager.getInstance().removeGroup();
        }
        RemoverManager.getInstance().removeCourse();
    }

    public void removeCourse(View v)
    {
        if(InternetConnection.isNetworkAvailable(getApplicationContext()))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmación")
                    .setMessage("Está seguro que quiere eliminar el curso?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            removeCourseAccept();
                            Intent i = new Intent(UserCourse_Info.this, UserCourses_Activity.class);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else InternetConnection.showNoInternet(getApplicationContext());
    }

    public void setInfo()
    {
        courseMap course_map = new courseMap();
        course = CourseSelected.getInstance().getCourse();
        tv_course_name.setText(course.getCourse_name());
        tv_course_name.setSelected(true);
        tv_code.setText(course.getCode());
        Log.e("Escuela", course.getSchool());
        if(course_map.getMap().containsKey(course.getSchool()))
            tv_school.setText(course_map.getMap().get(course.getSchool()));
        else tv_school.setText(course.getSchool());
        tv_school.setSelected(true);
        tv_credits.setText(Integer.toString(course.getCredits()));
        if(GroupsSelected.getInstance().getHash_groups().containsKey(CourseSelected.getInstance().getCourse().getCode()))
        {
            b_addgroup.setText("Ver Grupo");
        }
        else b_addgroup.setText("Agregar Grupo");
    }

    public void addGroup(View v)
    {
        //Ya tiene el grupo
        //Ver Grupo
        if(GroupsSelected.getInstance().getHash_groups().containsKey(CourseSelected.getInstance().getCourse().getCode()))
        {
            GroupSelected.getInstance().setGroup(GroupsSelected.getInstance().getHash_groups().get(CourseSelected.getInstance().getCourse().getCode()));
            Intent i = new Intent(UserCourse_Info.this,GroupInfo_Activity.class);
            i.putExtra(getString(R.string.lastActivity), true);
            startActivity(i);
        }
        //No lo tiene
        //Agregar Grupo
        else
        {
            Intent i = new Intent(UserCourse_Info.this,AllCourseInfo_Activity.class);
            i.putExtra(getString(R.string.lastActivity), false);
            startActivity(i);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_course__info, menu);
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
