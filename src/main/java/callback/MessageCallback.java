package callback;


import service.MessageProcessService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author xinbo.ju
 */


public class MessageCallback implements MqttCallback {
    private String payload;

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("连接断开，可以做重连");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("接收消息主题:" + topic);
        System.out.println("接收消息Qos:" + message.getQos());

        payload = new String(message.getPayload());

        System.out.println("Payload process start");
//        MessageProcessService messageProcessService = new MessageProcessService(payload);
//        messageProcessService.processHttpResponse();

        System.out.println("接收消息内容:" + new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}