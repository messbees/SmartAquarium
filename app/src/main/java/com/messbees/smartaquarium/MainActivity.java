package com.messbees.smartaquarium;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private PahoMqttClient pahoMqttClient;

    public static TextView temperatureView;
    private CheckBox notificationCheckbox;
    private Button enableButton;
    private SharedPreferences sharedPref;
    private Boolean isSubscribed;
    public static Boolean showNotification;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pahoMqttClient = new PahoMqttClient();
        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);
        intent = new Intent(MainActivity.this, MqttMessageService.class);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        isSubscribed = sharedPref.getBoolean("isSubscribed", false);
        showNotification = sharedPref.getBoolean("showNotification", false);

        temperatureView = (TextView) findViewById(R.id.temerature);
        notificationCheckbox = (CheckBox) findViewById(R.id.checkBox);
        if (showNotification) {
            notificationCheckbox.setChecked(true);
        }
        enableButton = (Button) findViewById(R.id.enableButton);
        if (!isSubscribed) {
            enableButton.setText("Включить");
            notificationCheckbox.setEnabled(false);
        }
        else {
            enableButton.setText("Выключить");
            notificationCheckbox.setEnabled(true);
        }

        enableButton.setOnClickListener(this);
        notificationCheckbox.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enableButton:
                try {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    Boolean result;
                    if (isSubscribed) {
                        pahoMqttClient.unSubscribe(client, Constants.SUBSCRIBE_TOPIC);
                        pahoMqttClient.publishMessage(client, "Unsubscribed", 2, Constants.PUBLISH_TOPIC);
                        enableButton.setText("Включить");
                        stopService(intent);
                        result = false;
                    }
                    else {
                        pahoMqttClient.subscribe(client, Constants.SUBSCRIBE_TOPIC, 2);
                        pahoMqttClient.publishMessage(client, "Subscribed", 2, Constants.PUBLISH_TOPIC);
                        enableButton.setText("Выключить");
                        startService(intent);
                        result = true;
                    }
                    notificationCheckbox.setEnabled(result);
                    editor.putBoolean("isSubscribed", result);
                    editor.commit();
                    isSubscribed = result;
                } catch (MqttException e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.checkBox:
                SharedPreferences.Editor editor = sharedPref.edit();
                if (notificationCheckbox.isChecked()) {
                    editor.putBoolean("showNotification", true);
                    showNotification = true;
                    Toast.makeText(this, "notification on", Toast.LENGTH_LONG).show();
                }
                else {
                    editor.putBoolean("showNotification", false);
                    showNotification = false;
                }
                editor.commit();
        }
    }
}
