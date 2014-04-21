package ver1.guiahorarios.progra1.UserInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ver1.guiahorarios.progra1.Connectivity.UserConn;
import ver1.guiahorarios.progra1.MainActivity;
import ver1.guiahorarios.progra1.Menu.MainMenu_Activity;
import ver1.guiahorarios.progra1.R;

public class Register_Activity extends Activity  {
    EditText tv_email;
    EditText tv_password;
    EditText tv_password2;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        tv_email = (EditText)findViewById(R.id.RA_E_Email);
        tv_password = (EditText)findViewById(R.id.RA_E_Pass);
        tv_password2= (EditText)findViewById(R.id.RA_E_Pass2);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Register_Activity.this, MainActivity.class);
        startActivity(i);
    }

    public void validateUser(View v)
    {
        if(tv_password.getText().toString().equals(tv_password2.getText().toString()))
        {
            email = tv_email.getText().toString();
            password = tv_password.getText().toString();
            if(validEntries(email,password))
            {
                new RetrieveFeedTask().execute();
            }
            else Toast.makeText(getApplicationContext(),"Todos los campos son necesarios",Toast.LENGTH_SHORT).show();

        }
        else Toast.makeText(getApplicationContext(),"Verfique que las contraseñas sean iguales",Toast.LENGTH_SHORT).show();
    }

    public boolean validEntries(String str2,String str3)
    {
        if(str2.length()>1 && str3.length()>1)
        {
            return true;
        }
        else return false;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_, menu);
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

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                UserConn.getInstance().postData(password,email);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            Intent i = new Intent(Register_Activity.this,MainMenu_Activity.class);
            startActivity(i);
        }
    }


}
