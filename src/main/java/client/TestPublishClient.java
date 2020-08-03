package client;

import callback.MessageCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;

/**
 * @author xinbo.ju
 */

public class TestPublishClient {
    public String host = "tcp://172.16.39.12:1883";

    public void publish(){
        String content = "what";
        String clientId = UUID.randomUUID().toString();

        try{
            MqttClient client = new MqttClient(host,clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.setCallback(new MessageCallback());

            client.connect(options);

            MqttMessage message = new MqttMessage(content.getBytes());
            client.publish("topic", message);
            client.disconnect();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
