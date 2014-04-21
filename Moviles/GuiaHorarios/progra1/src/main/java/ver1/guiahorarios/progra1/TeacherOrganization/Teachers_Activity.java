package ver1.guiahorarios.progra1.TeacherOrganization;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import ver1.guiahorarios.progra1.Connectivity.TeacherConn;
import ver1.guiahorarios.progra1.Menu.MainMenu_Activity;
import ver1.guiahorarios.progra1.R;

public class Teachers_Activity extends Activity implements AdapterView.OnItemClickListener,SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private ListView list_view;
    ArrayAdapter<Teacher> adapter;
    ArrayList<Teacher> teacher_list;
    SearchView inputSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_);
        list_view = (ListView) findViewById(R.id.UCA_LV);
        new RetrieveFeedTask().execute();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Teachers_Activity.this, MainMenu_Activity.class);
        startActivity(i);
    }


    @Override
    public boolean onClose() {
        Teachers_Activity.this.adapter.getFilter().filter("");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Teachers_Activity.this.adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Teachers_Activity.this.adapter.getFilter().filter(query);
        return false;
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        TeacherSelected.getInstance().setTeacher(adapter.getItem(position));
        Intent i = new Intent(this,TeacherInfo_Activity.class);
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_courses_, menu);
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

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                TeacherConn.getInstance().postData();
                teacher_list = TeacherConn.getInstance().getTeacher_list();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            adapter = new ArrayAdapter<Teacher>(Teachers_Activity.this, R.layout.list_item_mycourses, R.id.TV_course_name,teacher_list);
            list_view.setAdapter(adapter);
            list_view.setOnItemClickListener(Teachers_Activity.this);
        }
    }
}
