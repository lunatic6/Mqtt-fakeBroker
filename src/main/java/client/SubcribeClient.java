package client;

import callback.MessageCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author xinbo.ju
 */

public class SubcribeClient {
    private String host = "tcp://172.16.38.52:1883";
    private String topicFilePath = "src/main/resources/topics.txt";
    String[] topics;


    public void subcribe() {
        setTopics(topicFilePath);

        int[] qos = new int[topics.length];
        setTopics(this.topicFilePath);

        Arrays.fill(qos, 2);

        String clientid = UUID.randomUUID().toString();

        try {
            MqttClient client = new MqttClient(this.host, clientid, new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            client.setCallback(new MessageCallback());

            client.connect(options);
            System.out.println(Arrays.toString(topics));
            client.subscribe(topics, qos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // set topics array with configuration files
    private void setTopics(String path) {
        List<String> list = new ArrayList<>();
        InputStreamReader read = null;
        BufferedReader reader = null;
        StringBuilder s = new StringBuilder();

        try {
            File file = new File(path);
            read = new InputStreamReader(new FileInputStream(file));
            reader = new BufferedReader(read);

            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (read != null) {
                    read.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        topics = new String[list.size()];
        for (int i = 0; i < topics.length; i++) {
            topics[i] = list.get(i);
        }
    }

    public SubcribeClient(String topicFilePath, String host){
        this.topicFilePath = topicFilePath;
        this.host = host;
    }

    public SubcribeClient(){}

    public SubcribeClient(String host){
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTopicFilePath() {
        return topicFilePath;
    }

    public void setTopicFilePath(String topicFilePath) {
        this.topicFilePath = topicFilePath;
    }
}
