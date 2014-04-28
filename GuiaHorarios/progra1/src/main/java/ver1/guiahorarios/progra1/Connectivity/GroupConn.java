package ver1.guiahorarios.progra1.Connectivity;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import ver1.guiahorarios.progra1.CourseOrganization.CourseSelected;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupSelected;
import ver1.guiahorarios.progra1.CourseOrganization.GroupsSelected;
import ver1.guiahorarios.progra1.Schedule.ScheduleManager;

/**
 * Created by sanchosv on 18/04/14.
 */
public class GroupConn extends Observable {

    private ArrayList<String> facebook_ids = new ArrayList<String>();

    public ArrayList<String> getFacebook_ids() throws UnsupportedEncodingException, JSONException {
        getFacebookUsersGroup();
        return facebook_ids;
    }

    public void setFacebook_ids(ArrayList<String> facebook_ids) {
        this.facebook_ids = facebook_ids;
    }

    private ArrayList<Group> group_list = new ArrayList<Group>();

    public ArrayList<Group> getGroup_list()
    {
        return group_list;
    }

    private static GroupConn _instance;

    private GroupConn()
    {
    }

    public static GroupConn getInstance()
    {
        if(_instance==null)
        {
            _instance = new GroupConn();
        }
        return _instance;
    }

    public void getFacebookUsersGroup() throws JSONException, UnsupportedEncodingException {
        facebook_ids = new ArrayList<String>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/users/group");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("codigo", CourseSelected.getInstance().getCourse().getCode()));
        nameValuePair.add(new BasicNameValuePair("numero", Integer.toString(GroupSelected.getInstance().getGroup().getNumber())));

        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
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
        JSONArray jarray = new JSONArray(result);
        for (int i = 0; i < jarray.length(); i++) {

            try {
                JSONObject c = jarray.getJSONObject(i);
                String facebook_id = c.getString("facebook");
                facebook_ids.add(facebook_id);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveGroup(int pGroupNumber,String pPassword,String pEmail,String pCourseCode) throws JSONException {
        Log.e("Grupo a Guardar: ",""+pGroupNumber);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/insert/group");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        nameValuePair.add(new BasicNameValuePair("password", pPassword));
        nameValuePair.add(new BasicNameValuePair("correo", pEmail));
        nameValuePair.add(new BasicNameValuePair("curso", pCourseCode));
        nameValuePair.add(new BasicNameValuePair("numero", Integer.toString(pGroupNumber)));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            httpClient.execute(httpPost);
        }
        catch (UnsupportedEncodingException e) {
            // writing error to Log
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFacebookUserGroup(int pGroupNumber,String pFacebookID,String pCourseCode) throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/insert/group/facebook");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("facebookID", pFacebookID));
        nameValuePair.add(new BasicNameValuePair("curso", pCourseCode));
        nameValuePair.add(new BasicNameValuePair("numero", Integer.toString(pGroupNumber)));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            httpClient.execute(httpPost);
        }
        catch (UnsupportedEncodingException e) {
            // writing error to Log
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getGroupsPerCourse(String pCourseCode) throws JSONException, IOException {

            group_list = new ArrayList<Group>();
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://felialoismobile.herokuapp.com/groups/bycourse?curso="+pCourseCode);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            JSONArray jarray = new JSONArray(result);
            for (int i = 0; i < jarray.length(); i++) {
                try {
                    JSONObject c = jarray.getJSONObject(i);
                    String location = c.getString("sede");
                    JSONArray horario = c.getJSONArray("horario");
                    ArrayList<ArrayList<String>> schedule = getSchedule(horario);
                    int group_number = c.getInt("numero");
                    String teacher_name = c.getString("profesor");
                    String id = group_number+pCourseCode;
                    Group new_group = new Group();
                    new_group.setCourse_id(pCourseCode);
                    new_group.setNumber(group_number);
                    new_group.setLocation(location);
                    new_group.setSchedule(schedule);
                    new_group.setTeacher(teacher_name);
                    new_group.setId(id);
                    group_list.add(new_group);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

    public ArrayList<ArrayList<String>> getSchedule(JSONArray array) throws JSONException {
        ArrayList<ArrayList<String>> schedule = new ArrayList<ArrayList<String>>();
        for(int i = 0; i< array.length();i++)
        {
            ArrayList<String> info = new ArrayList<String>();
            info.add(array.getJSONObject(i).getString("aula"));
            info.add(array.getJSONObject(i).getString("hora"));
            info.add(array.getJSONObject(i).getString("dia"));
            schedule.add(info);
        }
        return  schedule;
    }

    public void getUserGroups(String pEmail,String pPassword) throws JSONException, IOException {

        GroupConn.getInstance().addObserver(ScheduleManager.getInstance());

        grupos_codigo = new ArrayList<String>();
        grupos_numero = new ArrayList<Integer>();

        GroupsSelected.getInstance().setHash_groups(new HashMap<String, Group>());

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/groups/user");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("password", pPassword));
        nameValuePair.add(new BasicNameValuePair("correo", pEmail));
        InputStream inputStream = null;
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
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
        JSONObject jarray = new JSONObject(result);
        JSONArray grupos = jarray.getJSONArray("grupos");
        getGroups(grupos);

        new GetGroupUsers().execute();
    }

    Group specific_group;

    public void getSpecificGroup(String pCodigo,int pNumero) throws JSONException, IOException {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/group");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("curso", pCodigo));
        nameValuePair.add(new BasicNameValuePair("numero", Integer.toString(pNumero)));
        InputStream inputStream = null;
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
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

        JSONObject c = new JSONObject(result);
        String location = c.getString("sede");
        JSONArray horario = c.getJSONArray("horario");
        ArrayList<ArrayList<String>> schedule = getSchedule(horario);
        int group_number = c.getInt("numero");
        String teacher_name = c.getString("profesor");
        String id = group_number+pCodigo;
        Group new_group = new Group();
        new_group.setCourse_id(pCodigo);
        new_group.setNumber(pNumero);
        new_group.setLocation(location);
        new_group.setSchedule(schedule);
        new_group.setTeacher(teacher_name);
        new_group.setId(id);

        specific_group = new_group;
    }

    public void getGroups(JSONArray array) throws JSONException {
        for(int i = 0; i< array.length();i++)
        {
            grupos_codigo.add(array.getJSONObject(i).getString("codigo"));
            grupos_numero.add(array.getJSONObject(i).getInt("numero"));
        }
    }

    int course_index;

    public void setUserGroups()
    {
        if(course_index<grupos_codigo.size())
        {
            new GetGroupUsers().execute();
        }
        else
        {
            course_index=0;
            Log.e("Termino", "Notifico");
            setChanged();
            notifyObservers();
        }
    }

    class GetGroupUsers extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                GroupConn.getInstance().getSpecificGroup(grupos_codigo.get(course_index),grupos_numero.get(course_index));
                GroupsSelected.getInstance().getHash_groups().put(specific_group.getCourse_id(), specific_group);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            course_index += 1;
            setUserGroups();
        }
    }


    ArrayList<String> grupos_codigo;
    ArrayList<Integer> grupos_numero;

    public void getFacebookUserGroups(String pFacebookId) throws JSONException, IOException {
        GroupConn.getInstance().addObserver(ScheduleManager.getInstance());

        GroupsSelected.getInstance().setHash_groups(new HashMap<String, Group>());
        grupos_codigo = new ArrayList<String>();
        grupos_numero = new ArrayList<Integer>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/groups/user/facebook");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("face", pFacebookId));


        InputStream inputStream = null;
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
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
        JSONObject jarray = new JSONObject(result);
        JSONArray grupos = jarray.getJSONArray("grupos");
        getGroups(grupos);
        new GetGroupUsers().execute();
     
    }
}
