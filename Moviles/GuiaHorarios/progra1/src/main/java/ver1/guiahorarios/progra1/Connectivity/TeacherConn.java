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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ver1.guiahorarios.progra1.TeacherOrganization.Teacher;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

/**
 * Created by sanchosv on 18/04/14.
 */
public class TeacherConn {

    private ArrayList<Teacher> teacher_list = new ArrayList<Teacher>();

    private boolean obtainedData = false;

    public ArrayList<Teacher> getTeacher_list()
    {
        return teacher_list;
    }

    private static TeacherConn _instance;

    private TeacherConn(){}

    public static TeacherConn getInstance()
    {
        if(_instance==null)
        {
            _instance = new TeacherConn();
        }
        return _instance;
    }

    public void saveRatingUser(String pTeacherName, double pRating) throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/rate/professor");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        nameValuePair.add(new BasicNameValuePair("password", UserChosen.getInstance().getUser().getPassword()));
        nameValuePair.add(new BasicNameValuePair("nombre",pTeacherName));
        nameValuePair.add(new BasicNameValuePair("correo", UserChosen.getInstance().getUser().getEmail()));
        nameValuePair.add(new BasicNameValuePair("calificacion", Double.toString(pRating)));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            httpClient.execute(httpPost);

            Log.e("FAFAFAFAF", "EXITO");
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

    public void saveRatingFacebook(String pTeacherName, double pRating) throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/rate/professor/facebook");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        nameValuePair.add(new BasicNameValuePair("nombre",pTeacherName));
        nameValuePair.add(new BasicNameValuePair("facebookID", UserChosen.getInstance().getUser().getFacebook()));
        nameValuePair.add(new BasicNameValuePair("calificacion", Double.toString(pRating)));
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

    public void postData() throws JSONException, IOException {

            teacher_list = new ArrayList<Teacher>();

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://felialoismobile.herokuapp.com/professors");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            HashMap<String, String> map = new HashMap<String, String>();
            JSONArray jarray = new JSONArray(result);
            for (int i = 0; i < jarray.length(); i++) {

                try {
                    JSONObject c = jarray.getJSONObject(i);
                    String name = c.getString("nombre");
                    int cantidad_cal = c.getInt("cantidadCalificaciones");
                    double calif_total = c.getDouble("calificacionTotal");
                    //int idProfesor = c.getInt("id_profe");

                    Teacher teacher = new Teacher();
                    if(calif_total==0) teacher.setAverageRating(0);
                    else teacher.setAverageRating(calif_total/cantidad_cal);
                   // teacher.setId(idProfesor);
                    teacher.setName(name);
                    teacher.setRatingTotal(cantidad_cal);
                    teacher_list.add(teacher);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

    }

}
