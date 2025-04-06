package example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.*;

@Singleton
@Startup
public class StartUpBean {

    @Resource(lookup = "jms/QueueConnectionFactory")
    ConnectionFactory queueConnectionFactory;

    @Resource(lookup = "jms/HelloQueue")
    Queue helloQueue;

    @PostConstruct
    private void postConstruct(){
        createQueueProducer();
    }

    private void createQueueProducer() {
        System.out.println("StartUpBean.createQueueProducer");

        try {
            Connection con = queueConnectionFactory.createConnection();
            con.start();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("helloQueue.getQueueName() = " + helloQueue.getQueueName());
            MessageProducer producer = session.createProducer(helloQueue);

            TextMessage message = session.createTextMessage("Hello Queue Startup");

            producer.send(message);

            session.close();
            con.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}