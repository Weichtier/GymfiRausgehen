package de.slowloris.gymfistudent;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Configuration {

    private static Configuration configuration;

    private Context instance;

    public Configuration(Context instance) {
        this.instance = instance;
    }

    public void save(JSONObject obj){
        FileOutputStream outputStream;

        try {
            outputStream = instance.openFileOutput("config.json", Context.MODE_PRIVATE);
            outputStream.write(obj.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getAsJSON(){
        FileInputStream inputStream;

        try {
            inputStream = instance.openFileInput("config.json");
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            String result = sb.toString();
            JSONObject obj = new JSONObject(result);
            return obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        Configuration.configuration = configuration;
    }
}
