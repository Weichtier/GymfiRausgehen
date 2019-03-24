package de.slowloris.gymfistudent;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class WebConnector {

    private Context instance;
    private String baseurl;

    public WebConnector(Context instance, String baseurl) {
        this.instance = instance;
        this.baseurl = baseurl;
    }

    public boolean validatePermission(){
        if(Utils.checkInternetConnection()){
            try {
                String request = Utils.sendWebRequest(instance, baseurl + "/check.php?id=" + Configuration.getConfiguration().getAsJSON().getInt("id") + "&mac=" + Utils.getMacAddress());

                JSONObject obj = new JSONObject(request);

                if(obj.getBoolean("haspermission")){
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int registerApp(){
        if(Utils.checkInternetConnection()){
            String request = Utils.sendWebRequest(instance, baseurl + "/register.php?mac=" + Utils.getMacAddress());
            try {
                JSONObject obj = new JSONObject(request);
                return obj.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }
}
