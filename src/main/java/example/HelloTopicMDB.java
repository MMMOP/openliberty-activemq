package example;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


@MessageDriven(
    mappedName = "HelloTopicMDB"
    , activationConfig = {
       @ActivationConfigProperty(propertyName = "clientId", propertyValue = "TEST.TOPIC.FOO.CLIENT.ID")
    }
)
public class HelloTopicMDB implements MessageListener {

    @PostConstruct
    public void init() {
    }

    @Override
    public void onMessage(Message message) {

        TextMessage msg = (TextMessage) message;
        try {
            System.out.println("HelloTopicMDB onMessage() >> " + msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}