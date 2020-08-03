package service;

import request.CameraSnapRequest;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xinbo.ju
 */

public class MessageProcessService {
    private Map<String, Object> map = new HashMap<>();
    private String payload;
    private String defaultDirPath = "D:\\Desktop\\Envision\\BMP\\Images";

    public MessageProcessService(String payload) {
        this.payload = payload;
    }

    public MessageProcessService(String payload, String defaultDirPath) {
        this.payload = payload;
        this.defaultDirPath = defaultDirPath;
    }

    public void processHttpResponse() {
        processBasedOnPayload(this.payload);

        String ip = map.get("ID").toString();

        System.out.println("send the snap request");
        CameraSnapRequest cameraSnapRequest = new CameraSnapRequest(ip);
        HttpResponse httpResponse = cameraSnapRequest.snap();

        String utc = map.get("UTC").toString();
        String eventType = map.get("eventType").toString();
        String date = utc.split(" ")[0];

        String timestamp = parseUTCToTimestamp(utc, "yyyy-MM-dd HH:mm:ss");
        String dateDirPath = defaultDirPath + "\\" + date + "\\" + ip;
        String filePath = ip + "_" + timestamp;

        System.out.println("image storage");
        imageLocalStorage(httpResponse, dateDirPath, filePath + "_" + eventType + ".jpeg");

        System.out.println("payload storage");
        payloadLocalStorage(payload, dateDirPath, filePath + ".json");

        System.out.println("Successfully process");
    }

    private void payloadLocalStorage(String payload, String dirPath, String filePath) {
        if (payload == null) {
            return;
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, filePath);
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            writer.write(payload);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Payload storage failed");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Writer close failed");
                }
            }
        }
    }

    private void imageLocalStorage(HttpResponse httpResponse, String dirPath, String filePath) {
        if (httpResponse == null || httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            return;
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        InputStream in = null;
        try {
            in = httpResponse.getEntity().getContent();
            File file = new File(dir, filePath);

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

        } catch (IOException e) {
            System.out.println("Invalid http response: image");
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    System.out.println("Output stream close error");
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("Input stream close error");
                }
            }
        }
    }

    private String parseUTCToTimestamp(String UTC, String patten) {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        String timestamp = "00000000000";

        try {
            timestamp = String.valueOf(sdf.parse(UTC).getTime() / 1000);
        } catch (ParseException e) {
            System.out.println("parse UTC failed");
        }

        return timestamp;
    }

    private void processBasedOnPayload(String payload) {
        Gson gson = new Gson();
        map = gson.fromJson(payload, map.getClass());
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getDefaultDirPath() {
        return defaultDirPath;
    }

    public void setDefaultDirPath(String defaultDirPath) {
        this.defaultDirPath = defaultDirPath;
    }
}
