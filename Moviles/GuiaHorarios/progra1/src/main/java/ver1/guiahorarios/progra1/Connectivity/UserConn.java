package ver1.guiahorarios.progra1.Connectivity;

/**
 * Created by sanchosv on 18/04/14.
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ver1.guiahorarios.progra1.CourseOrganization.Course;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupsSelected;
import ver1.guiahorarios.progra1.UserInfo.User;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

public class UserConn {

    private static UserConn _instance;

    public static boolean logInSuccess;

    private UserConn(){logInSuccess=false;}

    public static UserConn getInstance()
    {
        if(_instance==null)
        {
            _instance = new UserConn();
        }
        return _instance;
    }

    public void postData(String pPassword,String pEmail) throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/add/user");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("password", pPassword));
        nameValuePair.add(new BasicNameValuePair("correo", pEmail));
        nameValuePair.add(new BasicNameValuePair("facebook", "false"));
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
        User user = new User();
        user.setCourse_list(new ArrayList<Course>());
        user.setEmail(pEmail);
        user.setFacebook("false");
        user.setPassword(pPassword);
        UserChosen.getInstance().setUser(user);
    }

    public void logInFacebook(String pFacebookId) throws JSONException, UnsupportedEncodingException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/login/usuario/facebook");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("facebookID", pFacebookId));
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
        if(jarray.length()==0)
            logInSuccess = false;
        for (int i = 0; i < jarray.length(); i++) {

            try {
                JSONObject c = jarray.getJSONObject(i);
                //JSONArray cursos = c.getJSONArray("cursos");
                //JSONArray grupos = c.getJSONArray("grupos");
                //saveCourses(cursos);
                //saveGroups(grupos);
                logInSuccess = true;

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("JSON","SI LO HACE");

        if(logInSuccess)
        {
            User user = new User();
            //user.setCourse_list(new ArrayList<Course>());
            user.setEmail(" ");
            user.setFacebook(pFacebookId);
            //user.setFacebook("false");
            user.setPassword(" ");
            user.setHasFacebook(true);
            UserChosen.getInstance().setUser(user);
        }
    }

    public void logIn(String pPassword,String pEmail) throws JSONException, UnsupportedEncodingException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/login/usuario");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("password", pPassword));
        nameValuePair.add(new BasicNameValuePair("correo", pEmail));
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
        if(jarray.length()==0)
            logInSuccess = false;
        for (int i = 0; i < jarray.length(); i++) {

            try {
                JSONObject c = jarray.getJSONObject(i);
                //JSONArray cursos = c.getJSONArray("cursos");
                JSONArray grupos = c.getJSONArray("grupos");
                Log.e("SE CAE GRUPO","SI");
                //saveCourses(cursos);
                //saveGroups(grupos);
                logInSuccess = true;

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(logInSuccess)
        {
            User user = new User();
            //user.setCourse_list(new ArrayList<Course>());
            user.setEmail(pEmail);
            //user.setFacebook("false");
            user.setPassword(pPassword);
            user.setHasFacebook(false);
            UserChosen.getInstance().setUser(user);
        }
    }

    public void saveCourses(JSONArray pCourses) throws JSONException {

        for(int i =0;i<pCourses.length();i++)
        {
            JSONObject c = pCourses.getJSONObject(0);
            String name = c.getString("nombre");
            String escuela = c.getString("escuela");
            String codigo = c.getString("codigo");
            int creditos = c.getInt("creditos");
            Course course = new Course(name,codigo,creditos,escuela);
        }
    }

    public void saveGroups(JSONArray pGroups) throws JSONException {
        GroupsSelected.getInstance().getHash_groups().clear();
        for(int i=0;i<pGroups.length();i++)
        {
            JSONObject c = pGroups.getJSONObject(i);
            String location = c.getString("sede");
            JSONArray horario = c.getJSONArray("horario");
            ArrayList<ArrayList<String>> schedule = getSchedule(horario);
            int group_number = c.getInt("numero");
            String group_course = c.getString("curso");
            String teacher_name = c.getString("profesor");
            String id = group_number+ group_course;
            Group new_group = new Group();
            new_group.setCourse_id(group_course);
            new_group.setNumber(group_number);
            new_group.setLocation(location);
            new_group.setSchedule(schedule);
            new_group.setTeacher(teacher_name);
            new_group.setId(id);
            Log.e("TIENE GRUPO","SI");
            GroupsSelected.getInstance().getHash_groups().put(group_course,new_group);
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
}
