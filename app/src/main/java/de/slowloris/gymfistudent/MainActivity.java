package de.slowloris.gymfistudent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;
    private static File configFile;

    private TextView idText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        Configuration.setConfiguration(new Configuration(instance));
        configFile = new File(getFilesDir(), "config.json");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button validatebtn = (Button) findViewById(R.id.validatebtn);
        Button unregisterbtn = (Button) findViewById(R.id.unregisterbtn);
        idText = (TextView) findViewById(R.id.idText);

        validatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasperm = Utils.getWebConnector().validatePermission();

                String msg = "";
                int style;


                if(hasperm){
                    msg = "Du darfst das Schulgelände während der Pause verlassen!";
                    style = R.style.AlertDialogGreen;
                }else {
                    msg = "Du darfst das Schulgelände nicht verlassen!";
                    style = R.style.AlertDialogRed;
                }

                new AlertDialog.Builder(instance, style)
                        .setTitle("Berechtigung")
                        .setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });

        unregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configFile.delete();
                new AlertDialog.Builder(instance)
                        .setTitle("Entregistriert")
                        .setMessage("Du hast deine App ent-registriert! Starte sie neu um dich erneut zu registrieren!")
                        .create()
                        .show();
            }
        });

        if(!configFile.exists()){
            Intent registerIndent = new Intent(instance, RegisterActivity.class);
            startActivity(registerIndent);
        }else {
            try {
                Utils.setWebConnector(new WebConnector(instance, Configuration.getConfiguration().getAsJSON().getString("baseurl")));
                setId(Configuration.getConfiguration().getAsJSON().getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void setId(int id){
        idText.setText("Deine ID: " + id);
    }

}
