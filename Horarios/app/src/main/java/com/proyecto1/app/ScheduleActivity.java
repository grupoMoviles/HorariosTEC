package com.proyecto1.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ScheduleActivity extends Activity {

    CourseMenu root;
    ArrayAdapter<Group> adapter;
    ArrayList<Group> group_list;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        this.root = (CourseMenu)this.getLayoutInflater().inflate(R.layout.activity_schedule, null);
        this.setContentView(root);
        lv = (ListView) findViewById(R.id.LV_Sched);
        Bundle extras = getIntent().getExtras();
        group_list = extras.getParcelableArrayList("course_array");
        adapter = new ArrayAdapter<Group>(this, R.layout.list_item_schedcourses, R.id.TV_course_sched, group_list);
        lv.setAdapter(adapter);
    }

    public ArrayList<Group> getCourses()
    {
        ArrayList<Group> lista = new ArrayList<Group>();
        lista.add(new Group("Estadistica" ,"Geovani",1));
        lista.add(new Group("Moviles" , "Andrei",1));
        lista.add(new Group("Datos" , "Armando Arce",4));
        lista.add(new Group("Diseno" , "Jorge",2));
        lista.add(new Group("Seminario" , "Irene Aguilar",1));
        lista.add(new Group("Administracion de Proyectos" , "Ignacio Rivera",3));
        return lista;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_home)
        {
            toggleMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggleMenu(){
        this.root.moveMenu();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
            return rootView;
        }
    }

}
