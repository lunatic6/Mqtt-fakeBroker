package service;

import request.UploadToEnosRequest;

import java.io.File;

/**
 * @author xinbo.ju
 */

public class UploadService {
    private final String defaultDirPath = "D:\\Desktop\\Envision\\BMP\\Images";
    UploadToEnosRequest request = new UploadToEnosRequest();

    public UploadService() {

    }

    public void upload() {
        File defaultDir = new File(defaultDirPath);

        File[] dateDirs = defaultDir.listFiles();

        for (File dateDir : dateDirs) {

            for (File ipDir: dateDir.listFiles()) {
                String[] files = ipDir.list();
                int len = files.length;

                for (int i = 0; i < len; i += 2) {
                    String imageName = files[i];
                    String payloadName = files[i + 1];
                    String eventType = processImageName(imageName);

                    if (eventType == null) {
                        continue;
                    }

                    File image = new File(files[i]);
                    File payload = new File(files[++i]);
                    String success = request.uploadMeasurepoints(image, payload, eventType);

                    if ("OK".equals(success)) {
                        String uploadedImage = imageName.replace("jpeg", "png");
                        String uploadedPayload = payloadName.replace("json","txt");

                        image.renameTo(new File(ipDir, uploadedImage));
                        payload.renameTo(new File(ipDir, uploadedPayload));
                    }
                }
            }
        }
    }

    public String processImageName(String imageName) {
        String[] s = imageName.split("_");

        String temp = s[s.length - 1];
        s = temp.split("\\.");

        String eventType = s[0];
        String type = s[1];

        if ("jpeg".equals(eventType)) {
            eventType = null;
        }

        return eventType;
    }

}
