package ver1.guiahorarios.progra1.CourseOrganization;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import ver1.guiahorarios.progra1.Connectivity.CourseConn;
import ver1.guiahorarios.progra1.R;

public class AllCourses_Activity extends Activity {

    ExpandableListView expList;
    ExpandableListCourseAdapter adapter;
    private ArrayList<School> schoolList = new ArrayList<School>();
    private ArrayList<Course> selectedCourses = new ArrayList<Course>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses_);
        expList = (ExpandableListView)findViewById(R.id.ACA_ELV);
        new RetrieveFeedTask().execute();
        CoursesSelected.getInstance();
        fillCourseHash();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AllCourses_Activity.this,Course_Activity.class);
        startActivity(i);
    }

    public void fillCourseHash()
    {
        CoursesSelected.getInstance().fillCourseHash();
        CoursesSelected.getInstance().getHash_courses();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_courses_, menu);
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
                CourseConn.getInstance().postData();
                schoolList = CourseConn.getInstance().getSchoolList();
                //GroupConn.getInstance().postData();


            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            adapter = new ExpandableListCourseAdapter(AllCourses_Activity.this, expList, schoolList);
            expList.setAdapter(adapter);
            expList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    CourseSelected.getInstance().setCourse(schoolList.get(groupPosition).courseList.get(childPosition));

                    Log.e("ALL Courses Activity",CourseSelected.getInstance().getCourse().getCode());

                    Intent i = new Intent(AllCourses_Activity.this,AllCourseInfo_Activity.class);
                    startActivity(i);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            });
        }
    }

}
