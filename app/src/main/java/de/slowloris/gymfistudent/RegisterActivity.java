package de.slowloris.gymfistudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button registerButton = (Button) findViewById(R.id.registerbtn);
        final EditText serverUrlText = (EditText) findViewById(R.id.serverurlText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setWebConnector(new WebConnector(MainActivity.instance, serverUrlText.getText().toString()));

                int id = Utils.getWebConnector().registerApp();

                if(id == 0){
                    new AlertDialog.Builder(MainActivity.instance)
                            .setTitle("Fehler")
                            .setMessage("Es gab einen Fehler beim Registrieren der App! Bitte versuche es erneut")
                            .show();
                    return;
                }

                JSONObject obj = new JSONObject();

                try {
                    obj.put("baseurl", serverUrlText.getText().toString());
                    obj.put("id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Configuration.getConfiguration().save(obj);
                try {
                    MainActivity.instance.setId(obj.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();

            }
        });

    }

}
