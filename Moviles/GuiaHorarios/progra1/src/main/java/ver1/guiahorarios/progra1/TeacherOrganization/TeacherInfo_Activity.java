package ver1.guiahorarios.progra1.TeacherOrganization;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;

import ver1.guiahorarios.progra1.Connectivity.TeacherConn;
import ver1.guiahorarios.progra1.R;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

public class TeacherInfo_Activity extends Activity {

    TextView name_tv;
    TextView average_rating_tv;
    TextView total_rating_tv;
    RatingBar rating_bar;
    Teacher teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info_);
        name_tv = (TextView)findViewById(R.id.TIA_TV_Name);
        average_rating_tv = (TextView)findViewById(R.id.TIA_TV_AverageRating);
        total_rating_tv = (TextView)findViewById(R.id.TIA_TV_CantCal);
        rating_bar = (RatingBar)findViewById(R.id.TIA_Rating);
        setInfo();
    }

    public void setInfo()
    {
        teacher = TeacherSelected.getInstance().getTeacher();
        name_tv.setText(teacher.getName());
        average_rating_tv.setText(Double.toString(teacher.getAverageRating()));
        total_rating_tv.setText(Integer.toString(teacher.getRatingTotal()));
    }

    public void saveInfo(View v) throws JSONException {
        if(UserChosen.getInstance().getUser().isHasFacebook())
        {
            new saveRatingFacebookClass().execute();
            //TeacherConn.getInstance().saveRatingFacebook(teacher.getName(),amount_stars);
        }
        else
        {
            //TeacherConn.getInstance().saveRatingUser(teacher.getName(), amount_stars);
            new saveRatingUserClass().execute();
        }
        Intent i = new Intent(TeacherInfo_Activity.this,Teachers_Activity.class);
        startActivity(i);
    }

    class saveRatingFacebookClass extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                TeacherConn.getInstance().saveRatingFacebook(teacher.getName(),rating_bar.getRating());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }

    class saveRatingUserClass extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                Log.e("ESTREALLAS", "" + rating_bar.getNumStars());
                TeacherConn.getInstance().saveRatingUser(teacher.getName(), rating_bar.getRating());
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_info_, menu);
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

}
