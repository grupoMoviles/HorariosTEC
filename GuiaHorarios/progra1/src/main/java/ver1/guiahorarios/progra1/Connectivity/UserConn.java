package ver1.guiahorarios.progra1.Connectivity;

/**
 * Created by sanchosv on 18/04/14.
 */

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
        user.setHasFacebook(false);
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
                logInSuccess = true;

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(logInSuccess)
        {
            User user = new User();
            user.setEmail(" ");
            user.setFacebook(pFacebookId);
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
                JSONArray grupos = c.getJSONArray("grupos");
                logInSuccess = true;

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(logInSuccess)
        {
            User user = new User();
            user.setEmail(pEmail);
            user.setPassword(pPassword);
            user.setHasFacebook(false);
            UserChosen.getInstance().setUser(user);
        }
    }
}
