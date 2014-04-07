package com.proyecto1.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class MenuActivity extends Activity implements AdapterView.OnItemClickListener,SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    private ListView list_view;

    ArrayAdapter<Group> adapter;
    ArrayList<Group> group_list;

    SearchView inputSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        list_view = (ListView) findViewById(R.id.LV_MyCourses);
        list_view.setOnItemClickListener(this);

        Bundle extras = getIntent().getExtras();
        group_list = extras.getParcelableArrayList("course_array");
        adapter = new ArrayAdapter<Group>(this, R.layout.list_item_mycourses, R.id.TV_course_name, group_list);
        list_view.setAdapter(adapter);
    }

    @Override
    public boolean onClose() {
        MenuActivity.this.adapter.getFilter().filter("");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        MenuActivity.this.adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        MenuActivity.this.adapter.getFilter().filter(query);
        return false;
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent i = new Intent(this,CourseInfoActivity.class);
        i.putExtra("Name",adapter.getItem(position).course_name);
        i.putExtra("Group", adapter.getItem(position).group_number);
        i.putExtra("Teacher",adapter.getItem(position).teacher);
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        inputSearch = (SearchView) menu.findItem(R.id.search).getActionView();
        inputSearch.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        inputSearch.setIconifiedByDefault(false);
        inputSearch.setOnQueryTextListener(this);
        inputSearch.setOnCloseListener(this);
        inputSearch.clearFocus();
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

    public void switchSchedule(View v)
    {
        Intent i = new Intent(this,ScheduleActivity.class);
        i.putExtra("course_array", group_list);
        startActivity(i);
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
            View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
            return rootView;
        }
    }

}
