package de.slowloris.gymfistudent;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

public class Utils {

    private static WebConnector webConnector;

    public static String sendWebRequest(Context instance, String url){
        try {
            Document doc = Jsoup.connect(url).get();

            return doc.body().html();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Jsoup.parse("error").html();
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }else {
            new AlertDialog.Builder(MainActivity.instance, R.style.AlertDialogRed)
                    .setTitle("Internet benötigt")
                    .setMessage("Für diese Aktion benötigst du eine aktive Internetverbindung!")
                    .create()
                    .show();

            return false;
        }
    }

    public static WebConnector getWebConnector() {
        return webConnector;
    }

    public static void setWebConnector(WebConnector webConnector) {
        Utils.webConnector = webConnector;
    }
}
