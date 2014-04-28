package ver1.guiahorarios.progra1.CourseOrganization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ver1.guiahorarios.progra1.Connectivity.InternetConnection;
import ver1.guiahorarios.progra1.ConnectivityManager.SchoolCoursesManager;
import ver1.guiahorarios.progra1.Menu.MainMenu_Activity;
import ver1.guiahorarios.progra1.R;

public class AllCourses_Activity extends Activity implements Observer{

    ExpandableListView expList;
    ExpandableListCourseAdapter adapter;
    private ArrayList<School> schoolList = new ArrayList<School>();
    private ProgressBar progress_bar;
    private Button reload_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses_);
        expList = (ExpandableListView)findViewById(R.id.ACA_ELV);
        progress_bar = (ProgressBar)findViewById(R.id.ACA_ProgressBar);
        reload_button = (Button)findViewById(R.id.ACA_B_Reload);
        reload_button.setVisibility(View.INVISIBLE);
        SchoolCoursesManager.getInstance().addObserver(this);
        if(InternetConnection.isNetworkAvailable(getApplicationContext()))
        {
            progress_bar.setVisibility(View.VISIBLE);
            reload_button.setVisibility(View.INVISIBLE);
            SchoolCoursesManager.getInstance().getSchoolCourses();
        }
        else
        {
            reload_button.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.INVISIBLE);
            InternetConnection.showNoInternet(getApplicationContext());
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(AllCourses_Activity.this,MainMenu_Activity.class);
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

    public void reload(View v)
    {
        if(InternetConnection.isNetworkAvailable(getApplicationContext()))
        {
            progress_bar.setVisibility(View.VISIBLE);
            reload_button.setVisibility(View.INVISIBLE);
            SchoolCoursesManager.getInstance().addObserver(this);
            SchoolCoursesManager.getInstance().getSchoolCourses();
            CoursesSelected.getInstance();
            fillCourseHash();
            progress_bar.setVisibility(View.INVISIBLE);
        }
        else
        {
            progress_bar.setVisibility(View.INVISIBLE);
            InternetConnection.showNoInternet(getApplicationContext());
        }
    }

    @Override
    public void update(Observable arg0, Object arg1)
    {
        CoursesSelected.getInstance();
        fillCourseHash();
        progress_bar.setVisibility(View.INVISIBLE);
        schoolList = SchoolCoursesManager.getInstance().schoolList;
        adapter = new ExpandableListCourseAdapter(AllCourses_Activity.this, expList, schoolList);
        expList.setAdapter(adapter);
        expList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CourseSelected.getInstance().setCourse(schoolList.get(groupPosition).courseList.get(childPosition));
                Intent i = new Intent(AllCourses_Activity.this,AllCourseInfo_Activity.class);
                i.putExtra(getString(R.string.lastActivity), true);
                startActivity(i);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }
}
