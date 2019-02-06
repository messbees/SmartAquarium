package com.messbees.smartaquarium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {
    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private PahoMqttClient pahoMqttClient;

    public static TextView temperatureView;
    private CheckBox notificationCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final Intent intent = new Intent(MainActivity.this, MqttMessageService.class);

        temperatureView = (TextView) findViewById(R.id.temerature);
        notificationCheckbox = (CheckBox) findViewById(R.id.checkBox);
//        notificationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (notificationCheckbox.isChecked() == true) {
//                    startService(intent);
//                }
//                else {
//                    stopService(intent);
//                }
//            }
//        });
//
//        try {
//            pahoMqttClient.subscribe(client, "/ESP_Easy/ds18b20/temperature", 2);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//
//        startService(intent);
    }
}
