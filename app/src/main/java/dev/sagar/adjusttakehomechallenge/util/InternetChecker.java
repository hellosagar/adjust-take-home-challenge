package dev.sagar.adjusttakehomechallenge.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

/**
 * This class is using to get the current status of internet connectivity
 */
public class InternetChecker {

    private final Context context;

    public InternetChecker(Context context) {
        this.context = context;
    }

    public boolean hasInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE
        );
        Network activeNetwork = connectivityManager.getActiveNetwork();
         if (activeNetwork == null){
             return false;
         }
        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork);
         if (capabilities == null){
             return false;
         }

         if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
             return true;
         }

        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
            return true;
        }

        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
            return true;
        }

        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)){
            return true;
        }

        return false;
    }

}
