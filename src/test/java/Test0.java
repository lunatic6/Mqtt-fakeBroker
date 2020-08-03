
import callback.MessageCallback;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.paho.client.mqttv3.*;
import org.junit.Test;


import java.io.*;

import java.util.*;


public class Test0 {
    @Test
    public void test0000(){
        String url = "https://p6-bk.byteimg.com/tos-cn-i-mlhdmxsy5m/f9bccbe6733e43fcbc0c22b25f7a7572~tplv-mlhdmxsy5m-q75:1080:1080.image";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);

        }catch (Exception e){
            System.out.println("get response error");
        }

        System.out.println(httpResponse.getStatusLine().getStatusCode());

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        InputStream in = null;
        try {
            in = httpResponse.getEntity().getContent();
            File file = new File("D:\\Desktop\\Envision\\BMP\\Images\\a.png");

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1){
                bos.write(buffer,0,len);
            }

        }catch (IOException e){
            System.out.println("Invalid http response: image");
        }finally {
            if (bos != null){
                try{
                    bos.close();
                }catch (IOException e){
                    System.out.println("Output stream close error");
                }
            }

            if (in != null){
                try{
                    in.close();
                }catch (IOException e){
                    System.out.println("Input stream close error");
                }
            }
        }
    }

    @Test
    public void test3(){
        String host = "tcp://172.20.33.238:1883";
//        String host = "tcp://172.16.39.12:1883";
        String content = "{\"ID\":\"172.16.39.12\"," +
                "\"UTC\":\"2018-04-10 15:58:21\"," +
                "\"ObjectID\":\"007\"," +
                "\"ObjectClass\":\"ObjectClass\"," +
                "\"eventType\":\"loitering\"," +
                "\"ruleId\":001}";
        String clientId = UUID.randomUUID().toString();

        try{
            MqttClient client = new MqttClient(host,clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.setCallback(new MessageCallback());

            client.connect(options);

            MqttMessage message = new MqttMessage(content.getBytes());

            client.publish("loitering", message);
//            client.disconnect();
        }catch (Exception e){
            System.out.println(e.getCause());
        }
    }

    @Test
    public void tt(){
        String payload = "{\"ID\":\"172.16.39.12\"," +
                "\"UTC\":\"2018-04-10 15:58:21\"," +
                "\"ObjectID\":\"007\"," +
                "\"ObjectClass\":\"ObjectClass\"," +
                "\"eventType\":\"loitering\"," +
                "\"ruleId\":001}";

        Map<String, Object> map = new HashMap<>();
        Gson gson = new Gson();
        map = gson.fromJson(payload,map.getClass());
        System.out.println(map.get("ruleId").toString());
    }

    @Test
    public void ttt() throws IOException {
        String s = "D:\\Desktop\\Envision\\BMP\\Images";
        File file = new File(s);
        File[] list = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("jpeg")){
                    return true;
                }else {
                    return false;
                }
            }
        });

        for (File e: list){
            System.out.println(e.getName());
        }
    }
}
