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
import android.widget.TextView;

import com.facebook.Session;

import java.util.Observable;
import java.util.Observer;

import ver1.guiahorarios.progra1.Connectivity.GroupConn;
import ver1.guiahorarios.progra1.CourseOrganization.AllCourses_Activity;
import ver1.guiahorarios.progra1.CourseOrganization.CoursesSelected;
import ver1.guiahorarios.progra1.CourseOrganization.GroupsSelected;
import ver1.guiahorarios.progra1.CourseOrganization.UserCourses_Activity;
import ver1.guiahorarios.progra1.FacebookConn.FacebookUser;
import ver1.guiahorarios.progra1.MainActivity;
import ver1.guiahorarios.progra1.R;
import ver1.guiahorarios.progra1.Schedule.Schedule1;
import ver1.guiahorarios.progra1.Schedule.ScheduleActivity;
import ver1.guiahorarios.progra1.TeacherOrganization.Teachers_Activity;
import ver1.guiahorarios.progra1.UserInfo.UserChosen;

public class MainMenu_Activity extends Activity implements Observer {

    private TextView mCursos;
    private TextView mGrupos;
    private TextView mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_);
        Schedule1.getInstance();
        Schedule1.getInstance().initialize();

        mCursos = (TextView)findViewById(R.id.txtcursos);
        mGrupos=(TextView)findViewById(R.id.txtgrupos);
        mUsername=(TextView)findViewById(R.id.txtusr);
        //getScheduleArray();
        GroupConn.getInstance().addObserver((Observer) this);

        //PONGA AQUI LOS TEXTOS
        //////////////////////////////
        if(UserChosen.getInstance().getUser().isHasFacebook())
            mUsername.setText("Bienvenido: "+FacebookUser.getInstance().getUser().getFirstName());
        else mUsername.setText("Bienvenido: "+ UserChosen.getInstance().getUser().getEmail());
        mGrupos.setText("Grupos: "+(GroupsSelected.getInstance().getHash_groups().size()));
        mCursos.setText("Cursos: "+(CoursesSelected.getInstance().getCourse_list().size()));
        //Log.e("Cantidad Grupos1", ""+GroupsSelected.getInstance().getHash_groups().size());
        //Log.e("Cantidad Cursos1", ""+ CoursesSelected.getInstance().getCourse_list().size());
        //TAMBIEN EN LA ULTIMA FUNCION QUE SE LLAMA UPDATE
        /////////////////////////////////////////////////////

        //Log.e("NOMBRE FACE", FacebookUser.getInstance().getUser().getFirstName());
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


    public void callFacebookLogout(Context context) {
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

    public void switchTeachers(View v)
    {
        Intent i = new Intent(MainMenu_Activity.this, Teachers_Activity.class);
        startActivity(i);
    }

    public void switchScheduleActivity(View v)
    {
        Intent i = new Intent(MainMenu_Activity.this, ScheduleActivity.class);
        startActivity(i);
    }

    public void switchUserCourses(View v)
    {
        Intent i = new Intent(MainMenu_Activity.this,UserCourses_Activity.class);
        startActivity(i);
    }

    public void switchAllCourses(View v)
    {
        Intent i = new Intent(MainMenu_Activity.this,AllCourses_Activity.class);
        startActivity(i);
    }


    @Override
    public void update(Observable observable, Object o) {

        Log.e("LLEGA AL UPDATE", "SIIII");

        mGrupos.setText("Grupos: "+GroupsSelected.getInstance().getHash_groups().size());
        mCursos.setText("Cursos: "+CoursesSelected.getInstance().getCourse_list().size());
        //Ponga textView.setText(6 grupos) (X cursos)//
        //Log.e("Cantidad Grupos", ""+GroupsSelected.getInstance().getHash_groups().size());
        //Log.e("Cantidad Cursos", ""+ CoursesSelected.getInstance().getCourse_list().size());

    }
}

