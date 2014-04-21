package ver1.guiahorarios.progra1.Connectivity;

import android.os.AsyncTask;
import android.util.Log;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ver1.guiahorarios.progra1.CourseOrganization.GroupsSelected;

/**
 * Created by sanchosv on 20/04/14.
 */
public class Prueba {

    public Prueba(){}

    public void getPrueba(String pPassword) throws JSONException, IOException {

        GroupsSelected.getInstance().getHash_groups().clear();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://felialoismobile.herokuapp.com/prueba");
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("face", pPassword));
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
        /*for (int i = 0; i < jarray.length(); i++) {

            try {
                JSONObject c = jarray.getJSONObject(i);
                String location = c.getString("sede");
                JSONArray horario = c.getJSONArray("horario");
                ArrayList<ArrayList<String>> schedule = getSchedule(horario);
                int group_number = c.getInt("numero");
                String teacher_name = c.getString("profesor");
                String curso = c.getString("curso");
                String id = group_number+curso;
                Group new_group = new Group();
                new_group.setCourse_id(curso);
                new_group.setNumber(group_number);
                new_group.setLocation(location);
                new_group.setSchedule(schedule);
                new_group.setTeacher(teacher_name);
                new_group.setId(id);

                Log.e("Grupo", curso);

                GroupsSelected.getInstance().getHash_groups().put(curso,new_group);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
        Log.e("LARGO",""+jarray.length());
    }


class llamarPrueba extends AsyncTask<String, Void, String> {

    private Exception exception;

    protected String doInBackground(String... urls) {
        try {
            getPrueba("1516452460");
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
        return "";
    }

    protected void onPostExecute(String feed) {

    }
}

    public void prueba()
    {
        new llamarPrueba().execute();
    }
}
