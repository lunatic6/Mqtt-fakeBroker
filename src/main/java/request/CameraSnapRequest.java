package request;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author xinbo.ju
 */

public class CameraSnapRequest {
    private String url;

    public CameraSnapRequest(String ip){
        this.url = "http://" + ip + "/snap.jpg";
    }

    public CameraSnapRequest(String ip, String size){
        this.url = "http://" + ip + "/snap.jpg?JpegSize=" + size;
    }

    public HttpResponse snap(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        this.url = "https://p6-bk.byteimg.com/tos-cn-i-mlhdmxsy5m/f9bccbe6733e43fcbc0c22b25f7a7572~tplv-mlhdmxsy5m-q75:1080:1080.image";
        HttpGet httpGet = new HttpGet(this.url);
        HttpResponse httpResponse = null;

        try {
            httpResponse = httpClient.execute(httpGet);

        }catch (Exception e){
            System.out.println("get response error");
        }
        return httpResponse;
    }

}


