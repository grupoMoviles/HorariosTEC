package ver1.guiahorarios.progra1.Connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by sanchosv on 26/04/14.
 */
public class InternetConnection {

    public static boolean  isNetworkAvailable(Context pContext) {
        ConnectivityManager connectivityManager  = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showNoInternet(Context pContext)
    {
        Toast.makeText(pContext, "Revise su conexión a internet",Toast.LENGTH_SHORT).show();
    }

}
