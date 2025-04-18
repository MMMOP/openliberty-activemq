package example;

import javax.annotation.PostConstruct;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


@MessageDriven(
    mappedName = "HelloQueueMDB"
)
public class HelloQueueMDB implements MessageListener {

    @PostConstruct
    public void init() {
    }

    @Override
    public void onMessage(Message message) {

        TextMessage msg = (TextMessage) message;
        try {
            System.out.println("HelloQueueMDB onMessage() >> " + msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}