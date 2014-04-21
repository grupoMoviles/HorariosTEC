package ver1.guiahorarios.progra1.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Session;

import java.util.ArrayList;

import ver1.guiahorarios.progra1.CourseOrganization.Course_Activity;
import ver1.guiahorarios.progra1.CourseOrganization.Group;
import ver1.guiahorarios.progra1.CourseOrganization.GroupsSelected;
import ver1.guiahorarios.progra1.MainActivity;
import ver1.guiahorarios.progra1.R;
import ver1.guiahorarios.progra1.Schedule.Schedule1;
import ver1.guiahorarios.progra1.Schedule.ScheduleActivity;
import ver1.guiahorarios.progra1.TeacherOrganization.Teachers_Activity;

public class MainMenu_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_);
        Schedule1.getInstance();
        Schedule1.getInstance().initialize();
        getScheduleArray();
    }

    public void getScheduleArray()
    {
        ArrayList<Group> grupos = new ArrayList<Group>(GroupsSelected.getInstance().getHash_groups().values());
        Log.e("##########", "" + grupos.size());
        for(int i=0;i< grupos.size();i++)
        {
            Schedule1.getInstance().fillRange(grupos.get(i).getSchedule());
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("Está seguro que quiere cerrar sesión?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callFacebookLogout(getApplicationContext());
                        Intent i = new Intent(MainMenu_Activity.this,MainActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void switchScheduleActivity(View v)
    {
        Intent i = new Intent(MainMenu_Activity.this, ScheduleActivity.class);
        startActivity(i);
    }

    public static void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
            //clear your preferences if saved

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_, menu);
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

    public void switchCourses(View v)
    {
        Intent i = new Intent(MainMenu_Activity.this, Course_Activity.class);
        startActivity(i);
    }

    public void switchTeachers(View v)
    {
        Intent i = new Intent(MainMenu_Activity.this, Teachers_Activity.class);
        startActivity(i);
    }

}
