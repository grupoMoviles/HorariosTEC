package ver1.guiahorarios.progra1.CourseOrganization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ver1.guiahorarios.progra1.Connectivity.InternetConnection;
import ver1.guiahorarios.progra1.ConnectivityManager.GroupConnManager;
import ver1.guiahorarios.progra1.ConnectivityManager.RemoverManager;
import ver1.guiahorarios.progra1.R;

public class AllCourseInfo_Activity extends Activity implements AdapterView.OnItemClickListener, Observer{

    TextView tv_course_name;
    TextView tv_credits;
    TextView tv_code;
    TextView tv_school;
    Course course;
    Button b_add_course;
    private ListView list_view;
    ArrayAdapter<Group> adapter;
    ArrayList<Group> group_list;
    Button reload_button;
    Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_course_info_);
        tv_code = (TextView)findViewById(R.id.ACIA_TV_Code);
        tv_course_name = (TextView)findViewById(R.id.ACIA_TV_Name);
        tv_credits = (TextView)findViewById(R.id.ACIA_TV_Credits);
        tv_school = (TextView)findViewById(R.id.ACIA_TV_School);
        b_add_course = (Button)findViewById(R.id.ACIA_B_SelectCourse);
        list_view = (ListView)findViewById(R.id.ACIA_LV_Groups);
        reload_button = (Button)findViewById(R.id.ACIA_B_Reload);
        reload_button.setVisibility(View.GONE);
        GroupConnManager.getInstance().addObserver(this);
        setInfo();
        checkCourseExistance();
        if(InternetConnection.isNetworkAvailable(getApplicationContext()))
        {
            GroupConnManager.getInstance().getGroupsPerCourse(course.getCode());
        }
        else
        {
            reload_button.setVisibility(View.VISIBLE);
            InternetConnection.showNoInternet(getApplicationContext());
        }
    }

    public void reloadGroupsInfo(View v)
    {
        if(InternetConnection.isNetworkAvailable(getApplicationContext()))
        {
            reload_button.setVisibility(View.GONE);
            GroupConnManager.getInstance().getGroupsPerCourse(course.getCode());
        }
        else
        {
            reload_button.setVisibility(View.VISIBLE);
            InternetConnection.showNoInternet(getApplicationContext());
        }
    }

    @Override
    public void onBackPressed() {
        Boolean wasOnAllCourses = getIntent().getExtras().getBoolean("lastActivity");
        if(wasOnAllCourses)
        {
            Intent i = new Intent(AllCourseInfo_Activity.this,AllCourses_Activity.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(AllCourseInfo_Activity.this,UserCourse_Info.class);
            startActivity(i);
        }

    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        GroupSelected.getInstance().setGroup(group_list.get(position));
        Intent i = new Intent(this,GroupInfo_Activity.class);
        Boolean wasOnAllCourses = getIntent().getExtras().getBoolean("lastActivity");
        if(wasOnAllCourses)
            i.putExtra(getString(R.string.lastActivity), false);
        else
            i.putExtra(getString(R.string.lastActivity), true);
        startActivity(i);
    }

    public void checkCourseExistance()
    {
        if(CoursesSelected.getInstance().getHash_courses().containsKey(course.getCode()))
        {
            b_add_course.setText("Eliminar");
        }
        else b_add_course.setVisibility(View.GONE);
    }

    public void setInfo()
    {
        course = CourseSelected.getInstance().getCourse();
        tv_course_name.setText(course.getCourse_name());
        tv_course_name.setSelected(true);
        tv_school.setSelected(true);
        tv_code.setText(course.getCode());
        tv_school.setText(course.getSchool());
        tv_credits.setText(Integer.toString(course.getCredits()));
    }

    public void buttonCourseSelect(View v) throws JSONException {
       removeCourse();
    }

    public void removeCourse()
    {
        CoursesSelected.getInstance().getHash_courses().remove(course.getCode());
        ArrayList<Course> courses = new ArrayList<Course>(CoursesSelected.getInstance().getHash_courses().values());
        CoursesSelected.getInstance().setCourse_list(courses);
        if(GroupsSelected.getInstance().getHash_groups().containsKey(CourseSelected.getInstance().getCourse().getCode()));
        {
            GroupSelected.getInstance().setGroup(GroupsSelected.getInstance().getHash_groups().get(course.getCode()));
            GroupsSelected.getInstance().getHash_groups().remove(CourseSelected.getInstance().getCourse().getCode());
            RemoverManager.getInstance().removeGroup();
        }
        RemoverManager.getInstance().removeCourse();
        checkCourseExistance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_course_info_, menu);
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

    @Override
    public void update(Observable arg0, Object arg1)
    {
        reload_button.setVisibility(View.GONE);
        group_list = GroupConnManager.getInstance().group_list;
        adapter = new ArrayAdapter<Group>(AllCourseInfo_Activity.this, R.layout.list_item_mycourses, R.id.TV_course_name, group_list);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(AllCourseInfo_Activity.this);
    }


}
