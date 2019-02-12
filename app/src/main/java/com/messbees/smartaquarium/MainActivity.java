package com.messbees.smartaquarium;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private PahoMqttClient pahoMqttClient;

    private TextView temperatureView, dateView;
    private CheckBox notificationCheckbox;
    private Button lightButton;
    private SharedPreferences sharedPref;

    public static Boolean light = false;

    public static Boolean showNotification;

    private AppCompatActivity context;

    private Intent intent;
    private String lastValue;
    private String lastDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Constants.setContext(context);
        MqttMessageService.setContext(context);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_actionbar_background);

        pahoMqttClient = new PahoMqttClient();
        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);
        intent = new Intent(MainActivity.this, MqttMessageService.class);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        lastValue = sharedPref.getString("lastValue", "");
        lastDate = sharedPref.getString("lastDate", "");
        showNotification = sharedPref.getBoolean("showNotification", false);

        lightButton = findViewById(R.id.lightButton);
        lightButton.setOnClickListener(this);

        dateView = findViewById(R.id.dateView);
        dateView.setText(lastDate);
        temperatureView = findViewById(R.id.temerature);
        temperatureView.setText(lastValue);
        temperatureView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastDate = dateView.getText().toString();
                lastValue = temperatureView.getText().toString();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lastValue", lastValue);
                editor.putString("lastDate", lastDate);
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        notificationCheckbox = findViewById(R.id.checkBox);
        if (showNotification) {
            notificationCheckbox.setChecked(true);
        }
        notificationCheckbox.setOnClickListener(this);

        try {

            client.connect(null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken mqttToken) {
                    try {
                        pahoMqttClient.subscribe(client, Constants.SUBSCRIBE_TOPIC, 2);
                        pahoMqttClient.subscribe(client, Constants.LIGHT_TOPIC, 2);
                        startService(intent);

                    } catch (MqttException e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    Toast.makeText(context, "Фэйлд ту коннект...", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (MqttException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBox:
                SharedPreferences.Editor editor = sharedPref.edit();
                if (notificationCheckbox.isChecked()) {
                    editor.putBoolean("showNotification", true);
                    showNotification = true;
                }
                else {
                    editor.putBoolean("showNotification", false);
                    showNotification = false;
                }
                editor.apply();
                break;

            case R.id.lightButton:
                try {
                    String l;
                    if (light)
                        l = "0";
                    else
                        l = "1";
                    pahoMqttClient.publishMessage(client, l, 2, Constants.LIGHT_TOPIC);
                }
                catch (MqttException e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                catch (UnsupportedEncodingException e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
