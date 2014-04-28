package ver1.guiahorarios.progra1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;

import ver1.guiahorarios.progra1.Connectivity.InternetConnection;
import ver1.guiahorarios.progra1.ConnectivityManager.LogInManager;
import ver1.guiahorarios.progra1.FacebookConn.FacebookUser;
import ver1.guiahorarios.progra1.Menu.MainMenu_Activity;
import ver1.guiahorarios.progra1.UserInfo.Register_Activity;

public class MainActivity extends Activity implements View.OnTouchListener, Observer{

    private TextView register_tv;
    private EditText e_email;
    private EditText e_password;
    String password;
    String email;
    ProgressBar progress_bar;
    private Button loginButton;
    private PendingAction pendingAction = PendingAction.NONE;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //printHashKey();

        register_tv = (TextView) findViewById(R.id.MA_TV_Registrar);
        register_tv.setOnTouchListener(this);
        e_email = (EditText)findViewById(R.id.MA_E_Username);
        e_password = (EditText)findViewById(R.id.MA_E_Password);
        loginButton = (Button) findViewById(R.id.MA_B_Facebook);
        progress_bar = (ProgressBar)findViewById(R.id.MA_ProgressBar);
        progress_bar.setVisibility(View.INVISIBLE);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        LogInManager.getInstance().addObserver(this);
        //printHashKey();
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginToFb();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != PendingAction.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                        exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
        }
    }

    public void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("ver1.guiahorarios.progra1",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("TEMPTAGHASH KEY:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    @Override
    public void onBackPressed() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Intent i = new Intent(MainActivity.this, Register_Activity.class);
        startActivity(i);
        return false;
    }

    public void customLogIn(View v)
    {
        password = e_password.getText().toString();
        email = e_email.getText().toString();
        if(password.length()>0 && email.length()>0)
        {
            if(InternetConnection.isNetworkAvailable(getApplicationContext()))
            {
                progress_bar.setVisibility(View.VISIBLE);
                LogInManager.getInstance().logInCustomUser(getApplicationContext(),email,password);
            }
            else InternetConnection.showNoInternet(getApplicationContext());
        }
        else Toast.makeText(getApplicationContext(),"Todos los campos son necesarios",Toast.LENGTH_SHORT).show();
    }

    private void loginToFb() {
        if(InternetConnection.isNetworkAvailable(getApplicationContext()))
        {
            progress_bar.setVisibility(View.VISIBLE);
            Session.openActiveSession(this, true, new Session.StatusCallback() {

                // callback when session changes state
                @Override
                public void call(Session session, SessionState state,
                                 Exception exception) {
                    if (session.isOpened()) {
                        // make request to the /me API
                        Request request = Request.newMeRequest(session,
                                new Request.GraphUserCallback() {

                                    // callback after Graph API response with user
                                    // object
                                    @Override
                                    public void onCompleted(GraphUser user,
                                                            Response response) {
                                        if (user != null) {
                                            FacebookUser.getInstance().setUser(user);
                                            progress_bar.setVisibility(View.VISIBLE);
                                            LogInManager.getInstance().logInFacebookUser(getApplicationContext());
                                        }
                                    }
                                }
                        );
                        Request.executeBatchAsync(request);
                    }
                }
            });
        }
        else InternetConnection.showNoInternet(getApplicationContext());
    }


    @Override
    public void update(Observable arg0, Object arg1)
    {
        String param = (String)arg1;
        if(param.equals("YES"))
        {
            Intent i = new Intent(MainActivity.this, MainMenu_Activity.class);
            startActivity(i);
        }
        else progress_bar.setVisibility(View.INVISIBLE);
    }
}
