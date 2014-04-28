package ver1.guiahorarios.progra1.FacebookConn;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ver1.guiahorarios.progra1.Connectivity.GroupConn;
import ver1.guiahorarios.progra1.Connectivity.InternetConnection;
import ver1.guiahorarios.progra1.R;

public class Friends_Activity extends Activity {


    HashMap<String,String> friends_hash;
    ArrayList<String> friends_list;
    ListView list_view;
    List<RowItem> rowItems;
    ArrayList<String> people_in_group;
    TextView noAmigos;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_);
        friends_hash = new HashMap<String, String>();
        friends_list = new ArrayList<String>();
        list_view = (ListView)findViewById(R.id.FA_LV);
        people_in_group = new ArrayList<String>();
        noAmigos = (TextView)findViewById(R.id.FA_TV_Amigos);
        noAmigos.setVisibility(View.INVISIBLE);
        progress_bar = (ProgressBar)findViewById(R.id.FA_ProgressBar);
        if(InternetConnection.isNetworkAvailable(getApplicationContext()))
        {
            progress_bar.setVisibility(View.VISIBLE);
            getFriends();
        }
        else
        {
            progress_bar.setVisibility(View.INVISIBLE);
            InternetConnection.showNoInternet(getApplicationContext());
        }
    }

    public void setInfo()
    {
        if(friends_list.size()>0)
            {
                rowItems = new ArrayList<RowItem>();
                for (int i = 0; i < friends_list.size(); i++) {
                    RowItem item = new RowItem(friends_list.get(i),friends_hash.get(friends_list.get(i)));
                    rowItems.add(item);
                }
                FriendsLVAdapter adapter = new FriendsLVAdapter(this,R.layout.friends_item, rowItems);
                list_view.setAdapter(adapter);
                progress_bar.setVisibility(View.INVISIBLE);
            }
            else
            {
                progress_bar.setVisibility(View.INVISIBLE);
                noAmigos.setVisibility(View.VISIBLE);
            }
    }

    private void getFriendsinGroup()
    {
        friends_list = new ArrayList<String>();

        for(int i = 0;i<people_in_group.size();i++)
        {
            if(friends_hash.containsKey(people_in_group.get(i)))
            {
                friends_list.add(people_in_group.get(i));
            }
        }
        setInfo();
    }

    class AsyncGetFriendsGroup extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                people_in_group  = GroupConn.getInstance().getFacebook_ids();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
            return "";
        }

        protected void onPostExecute(String feed) {
            getFriendsinGroup();
        }
    }

    private void getFriends(){
        Session activeSession = Session.getActiveSession();
        if(activeSession.getState().isOpened()){
            Request friendRequest = Request.newMyFriendsRequest(activeSession,
                    new Request.GraphUserListCallback(){
                        @Override
                        public void onCompleted(List<GraphUser> users,
                                                Response response)  {
                            JSONObject jsonObject = response.getGraphObject().getInnerJSONObject();
                            try {
                                JSONArray array = jsonObject.getJSONArray("data");
                                for(int i = 0; i< array.length();i++)
                                {
                                    friends_hash.put(Integer.toString(array.getJSONObject(i).getInt("id")), jsonObject.getJSONArray("data").getJSONObject(i).getString("name"));
                                }
                                new AsyncGetFriendsGroup().execute();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle params = new Bundle();
            params.putString("fields","id,name,picture");
            friendRequest.setParameters(params);
            friendRequest.executeAsync();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friends_, menu);
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
