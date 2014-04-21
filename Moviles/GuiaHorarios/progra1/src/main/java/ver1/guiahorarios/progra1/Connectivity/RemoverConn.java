package ver1.guiahorarios.progra1.Connectivity;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanchosv on 19/04/14.
 */
public class RemoverConn {

    private static RemoverConn _instance;

    private RemoverConn(){}

    public static RemoverConn getInstance()
    {
        if(_instance==null)
        {
            _instance = new RemoverConn();
        }
        return _instance;
    }

    public void deleteCourse(String pPassword,String pEmail,String pCourseCode) throws UnsupportedEncodingException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/delete/course/user");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("password", pPassword));
        nameValuePair.add(new BasicNameValuePair("correo", pEmail));
        nameValuePair.add(new BasicNameValuePair("codigo", pCourseCode));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        try {
            httpClient.execute(httpPost).getEntity();
        } catch (Exception e) {
            // Oops
        }
    }

    public void deleteGroup(String pPassword,String pEmail,String pCourseCode, int pGroupNumber) throws UnsupportedEncodingException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/delete/group/user");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        nameValuePair.add(new BasicNameValuePair("password", pPassword));
        nameValuePair.add(new BasicNameValuePair("correo", pEmail));
        nameValuePair.add(new BasicNameValuePair("curso", pCourseCode));
        nameValuePair.add(new BasicNameValuePair("numero", Integer.toString(pGroupNumber)));
        Log.e("2","MO");
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        try {
            httpClient.execute(httpPost).getEntity();
        } catch (Exception e) {
            // Oops
        }
    }

    public void deleteFacebookUserCourse(String pFacebookID,String pCourseCode) throws UnsupportedEncodingException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/delete/course/user/facebook");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("facebookID", pFacebookID));
        nameValuePair.add(new BasicNameValuePair("codigo", pCourseCode));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        try {
            httpClient.execute(httpPost).getEntity();
        } catch (Exception e) {
            // Oops
        }
    }

    public void deleteFacebookUserGroup(String pFacebookID,String pCourseCode, int pGroupNumber) throws UnsupportedEncodingException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/delete/group/user/facebook");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("facebookID", pFacebookID));
        nameValuePair.add(new BasicNameValuePair("curso", pCourseCode));
        nameValuePair.add(new BasicNameValuePair("numero", Integer.toString(pGroupNumber)));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        try {
            httpClient.execute(httpPost).getEntity();
        } catch (Exception e) {
            // Oops
        }
    }
}
