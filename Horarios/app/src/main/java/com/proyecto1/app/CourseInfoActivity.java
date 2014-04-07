package com.proyecto1.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CourseInfoActivity extends Activity {

    private String course_name;
    private String teacher;
    private int groupTest;
    private TextView TV_name;
    private TextView TV_teacher;
    private TextView TV_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        Bundle extras = getIntent().getExtras();
        course_name = extras.getString("Name");
        teacher = extras.getString("Teacher");
        groupTest = extras.getInt("Group");
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        setLabels();
    }

    public void setLabels()
    {
        TV_name = (TextView)findViewById(R.id.TV_Info_CourseName);
        TV_teacher = (TextView)findViewById(R.id.TV_InfoProfesor);
        TV_group = (TextView)findViewById(R.id.TV_InfoGroup);
        TV_name.setText(course_name);
        TV_teacher.setText(teacher);
        TV_group.setText(Integer.toString(groupTest));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course_info, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_course_info, container, false);
            return rootView;
        }
    }

}
