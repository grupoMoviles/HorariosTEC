package ver1.guiahorarios.progra1.CourseOrganization;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import ver1.guiahorarios.progra1.Connectivity.CourseConn;
import ver1.guiahorarios.progra1.Connectivity.GroupConn;
import ver1.guiahorarios.progra1.Connectivity.Remover;
import ver1.guiahorarios.progra1.R;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

public class AllCourseInfo_Activity extends Activity implements AdapterView.OnItemClickListener{

    TextView tv_course_name;
    TextView tv_credits;
    TextView tv_code;
    TextView tv_school;
    Course course;
    Button b_add_course;
    private ListView list_view;
    ArrayAdapter<Group> adapter;
    ArrayList<Group> group_list;
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
        setInfo();
        checkCourseExistance();
        new RetrieveFeedTask().execute();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AllCourseInfo_Activity.this,AllCourses_Activity.class);
        startActivity(i);
    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        GroupSelected.getInstance().setGroup(group_list.get(position));
       // TeacherSelected.getInstance().setTeacher(adapter.getItem(position));

        Log.e("ALL Courses INFO", CourseSelected.getInstance().getCourse().getCode());


        Intent i = new Intent(this,GroupInfo_Activity.class);
        i.putExtra(getString(R.string.lastActivity), false);
        startActivity(i);
    }

    public void checkCourseExistance()
    {
        if(CoursesSelected.getInstance().getHash_courses().containsKey(course.getCode()))
        {
            b_add_course.setText("Eliminar");
        }
        else b_add_course.setVisibility(View.INVISIBLE);
    }

    public void setInfo()
    {
        course = CourseSelected.getInstance().getCourse();
        tv_course_name.setText(course.getCourse_name());
        tv_code.setText(course.getCode());
        tv_school.setText(course.getSchool());
        tv_credits.setText(Integer.toString(course.getCredits()));
    }

    public void buttonCourseSelect(View v) throws JSONException {
        if(CoursesSelected.getInstance().getHash_courses().containsKey(course.getCode()))
            removeCourse();
        else addCourse();
    }

    public void addCourse() throws JSONException {
        CoursesSelected.getInstance().getCourse_list().add(course);
        CoursesSelected.getInstance().getHash_courses().put(course.getCode(), course);
        if(UserChosen.getInstance().getUser().isHasFacebook()){
            new AsyncSaveFacebookUserCourse().execute();
        }
        else
        new AsyncSaveCourse().execute();
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
            Remover.getInstance().removeGroup();
        }
        Remover.getInstance().removeCourse();
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

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                GroupConn.getInstance().postData(course.getCode());
                group_list = GroupConn.getInstance().getGroup_list();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            adapter = new ArrayAdapter<Group>(AllCourseInfo_Activity.this, R.layout.list_item_mycourses, R.id.TV_course_name, group_list);
            list_view.setAdapter(adapter);
            list_view.setOnItemClickListener(AllCourseInfo_Activity.this);
        }
    }

    class AsyncSaveCourse extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                CourseConn.getInstance().saveCourse(course, UserChosen.getInstance().getUser().getPassword(), UserChosen.getInstance().getUser().getEmail());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            checkCourseExistance();
        }
    }

    class AsyncSaveFacebookUserCourse extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                CourseConn.getInstance().saveFacebookUSerCourse(course, UserChosen.getInstance().getUser().getFacebook());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            checkCourseExistance();
        }
    }

}
