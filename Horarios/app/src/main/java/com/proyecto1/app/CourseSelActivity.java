package com.proyecto1.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CourseSelActivity extends Activity  {

    ExpandableListView expListView;
    private ExpandableListCourseAdapter listAdapter;
    private ArrayList<School> schoolList = new ArrayList<School>();
    private ArrayList<Group> selectedGroups = new ArrayList<Group>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_sel);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        new RetrieveFeedTask().execute();

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        expListView = (ExpandableListView) findViewById(R.id.ExpListCourse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course_sel, menu);
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_course_sel, container, false);
            return rootView;
        }
    }

    public void switchMenu(View v)
    {
        Intent i= new Intent(CourseSelActivity.this,MenuActivity.class);
        i.putExtra("course_array", getSelectedGroups());
        startActivity(i);
    }

    public ArrayList<Group> getSelectedGroups()
    {
        ArrayList<Group> selGroups = new ArrayList<Group>();
        for(int i= 0; i< selectedGroups.size();i++)
        {
            if(selectedGroups.get(i).selected)
                selGroups.add(selectedGroups.get(i));
        }
        selectedGroups = selGroups;
        return selGroups;
    }

    public void postData() throws JSONException {

        // Creating HTTP client
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.1.102:1212/courses");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("", ""));
        nameValuePair.add(new BasicNameValuePair("", ""));

        // Url Encoding the POST parameters
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        }
        catch (UnsupportedEncodingException e) {
            // writing error to Log
            e.printStackTrace();
        }
        // writing response to log
        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            // Oops
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }
        HashMap<String, String> map = new HashMap<String, String>();
        JSONArray jarray = new JSONArray(result);
        for (int i = 0; i < jarray.length(); i++) {

            try {
                JSONObject c = jarray.getJSONObject(i);
                String name = c.getString("nombre");
                String escuela = c.getString("escuela");
                String codigo = c.getString("codigo");
                int creditos = c.getInt("creditos");
                if(schoolExists(map,escuela))
                {
                    addCourseToSchool(new Group(name,codigo,creditos),escuela);
                }
                else
                {
                    map.put(escuela, escuela);
                    ArrayList<Group> grupo = new ArrayList<Group>();
                    grupo.add(new Group(name,codigo,creditos));
                    School school = new School(escuela,grupo);
                    schoolList.add(school);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean schoolExists(HashMap<String, String> map, String escuela)
    {
        if (map.containsKey(escuela)) return true;
        else return false;
    }

    public void addCourseToSchool(Group group, String escuela)
    {
           for (int i = 0; i<schoolList.size();i++)
           {
               if(schoolList.get(i).school_name.equals(escuela))
               {schoolList.get(i).groupList.add(group);break;}
           }
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                postData();

            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            listAdapter = new ExpandableListCourseAdapter(CourseSelActivity.this, expListView, schoolList);
            expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if(schoolList.get(groupPosition).groupList.get(childPosition).selected) {
                        schoolList.get(groupPosition).groupList.get(childPosition).selected = false;
                    } else {
                        schoolList.get(groupPosition).groupList.get(childPosition).selected = true;
                        selectedGroups.add(schoolList.get(groupPosition).groupList.get(childPosition));
                    }
                    listAdapter.notifyDataSetChanged();
                    return true;
                }
            });
        }
    }
}
