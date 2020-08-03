package request;

import com.envisioniot.enos.iot_http_integration.HttpConnection;
import com.envisioniot.enos.iot_http_integration.message.IntegrationMeasurepointPostRequest;
import com.envisioniot.enos.iot_http_integration.message.IntegrationResponse;
import com.envisioniot.enos.sdk.data.DeviceInfo;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

/**
 * @author xinbo.ju
 */


public class UploadToEnosRequest {
    public final String ORG_ID = "enos.app.orgid";
    public final String assetId = "5ROXPMVz";

    public String timestap;
    HttpConnection connection = new HttpConnection.Builder(
            "https://iot-http-ppe1.envisioniot.com",
            "http://apim-apigw-proxy.apaas-ppe1.eniot.io",
            "afc25592-c55f-43d1-8900-32707fcaabe4",
            "78ec38f5-e391-465a-9c64-47710fcc4f44",
            "o15550778057941")
            .build();

    public UploadToEnosRequest() {
    }

    public IntegrationMeasurepointPostRequest buildUploadRequest(File image, File payload, String eventType) {
        DeviceInfo deviceInfo = new DeviceInfo().setAssetId(assetId);
        HashMap<String, Object> map = new HashMap<>(3);
        map.put("event_type", eventType);
        map.put("image", image);
        map.put("payload", payload);

        return IntegrationMeasurepointPostRequest.builder().
                addMeasurepoint(deviceInfo, System.currentTimeMillis(), map).build();

    }

    public String uploadMeasurepoints(File image, File payload, String eventType) {
        IntegrationMeasurepointPostRequest request = buildUploadRequest(image, payload, eventType);

        try {
            IntegrationResponse response = connection.publish(request, (bytes, length) ->
                    System.out.println(String.format("Progress: %.2f %%", (float) bytes / length * 100.0)));

            System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(response));
            return response.getMsg();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
