package com.messbees.smartaquarium;

import android.content.Context;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private PahoMqttClient pahoMqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = pahoMqttClient.getMqttClient(getApplicationContext(), Constants.MQTT_BROKER_URL, Constants.CLIENT_ID);


        Button buttonPublish = (Button) findViewById(R.id.buttonPublish);
        buttonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pahoMqttClient.publishMessage(client, "328.1", 2, Constants.PUBLISH_TOPIC);
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });



    }
}
