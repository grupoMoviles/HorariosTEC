package ver1.guiahorarios.progra1.Connectivity;

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

import ver1.guiahorarios.progra1.CourseOrganization.Course;
import ver1.guiahorarios.progra1.CourseOrganization.CoursesSelected;
import ver1.guiahorarios.progra1.CourseOrganization.School;
import ver1.guiahorarios.progra1.CourseOrganization.courseMap;

/**
 * Created by sanchosv on 18/04/14.
 */
public class CourseConn {

    private ArrayList<School> schoolList = new ArrayList<School>();

    private ArrayList<String> user_courses_code;

    public ArrayList<String> getUser_courses_code() {
        return user_courses_code;
    }

    private ArrayList<String> user_courses_names;

    public ArrayList<String> getUser_courses_names() {
        return user_courses_names;
    }

    public void setUser_courses_code(ArrayList<String> user_courses_code) {
        this.user_courses_code = user_courses_code;
    }

    private boolean obtainedData = false;

    public ArrayList<School> getSchoolList()
    {
        return schoolList;
    }

    private static CourseConn _instance;

    private CourseConn(){}

    public static CourseConn getInstance()
    {
        if(_instance==null)
        {
            _instance = new CourseConn();
        }
        return _instance;
    }

    public void postData() throws JSONException, IOException {

        if(!obtainedData)
        {
            // Creating HTTP client
            //8
            courseMap cm = new courseMap();
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://felialoismobile.herokuapp.com/courses");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            HashMap<String, String> map = new HashMap<String, String>();
            JSONArray jarray = new JSONArray(result);
            for (int i = 0; i < jarray.length(); i++) {

                try {
                    JSONObject c = jarray.getJSONObject(i);
                    String name = c.getString("nombre");
                    String escuela = c.getString("escuela");
                    if(cm.getMap().containsKey(escuela))
                        escuela = cm.getMap().get(escuela);
                    String codigo = c.getString("codigo");
                    int creditos = c.getInt("creditos");
                    //int idCurso = c.getInt("course_id");

                    if(schoolExists(map,escuela))
                    {
                        addCourseToSchool(new Course(name,codigo,creditos,escuela),escuela);
                    }
                    else
                    {
                        map.put(escuela, escuela);
                        ArrayList<Course> course = new ArrayList<Course>();
                        course.add(new Course(name,codigo,creditos,escuela));
                        School school = new School(escuela,course);
                        schoolList.add(school);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            obtainedData = true;
        }

    }

    public void getUserCourses(String pEmail,String pPassword) throws JSONException, IOException {

        user_courses_code = new ArrayList<String>();
        user_courses_names = new ArrayList<String>();
        CoursesSelected.getInstance().getCourse_list().clear();
        CoursesSelected.getInstance().getHash_courses().clear();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/courses/user");
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
        JSONArray jarray = new JSONArray(result);
        for (int i = 0; i < jarray.length(); i++) {

            try {
                JSONObject c = jarray.getJSONObject(i);
                String name = c.getString("nombre");
                String escuela = c.getString("escuela");
                String codigo = c.getString("codigo");
                int creditos = c.getInt("creditos");
                CoursesSelected.getInstance().getCourse_list().add(new Course(name,codigo,creditos,escuela));
                CoursesSelected.getInstance().getHash_courses().put(codigo,new Course(name,codigo,creditos,escuela));
                user_courses_code.add(codigo);
                user_courses_names.add(name);
                }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void getFacebookUserCourses(String pFacebookId) throws JSONException, IOException {
        user_courses_code = new ArrayList<String>();
        user_courses_names = new ArrayList<String>();
        CoursesSelected.getInstance().getCourse_list().clear();
        CoursesSelected.getInstance().getHash_courses().clear();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/courses/user/facebook");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("facebookID", pFacebookId));
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
        JSONArray jarray = new JSONArray(result);
        for (int i = 0; i < jarray.length(); i++) {

            try {
                JSONObject c = jarray.getJSONObject(i);
                String name = c.getString("nombre");
                String escuela = c.getString("escuela");
                String codigo = c.getString("codigo");
                int creditos = c.getInt("creditos");
                CoursesSelected.getInstance().getCourse_list().add(new Course(name,codigo,creditos,escuela));
                CoursesSelected.getInstance().getHash_courses().put(codigo,new Course(name,codigo,creditos,escuela));
                user_courses_code.add(codigo);
                user_courses_names.add(name);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void saveCourse(Course pCourse,String pPassword,String pEmail) throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/insert/course");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("password", pPassword));
        nameValuePair.add(new BasicNameValuePair("correo", pEmail));
        nameValuePair.add(new BasicNameValuePair("codigo", pCourse.getCode()));
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

    public void saveFacebookUSerCourse(Course pCourse,String pFacebookID) throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/insert/course/facebook");

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("facebookID", pFacebookID));
        nameValuePair.add(new BasicNameValuePair("codigo", pCourse.getCode()));
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

    public boolean schoolExists(HashMap<String, String> map, String escuela)
    {
        if (map.containsKey(escuela)) return true;
        else return false;
    }

    public void addCourseToSchool(Course course, String escuela)
    {
        for (int i = 0; i<schoolList.size();i++)
        {
            if(schoolList.get(i).school_name.equals(escuela))
            {schoolList.get(i).courseList.add(course);break;}
        }
    }

}
