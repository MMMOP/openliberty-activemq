package example;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Singleton
@Startup
public class StartUpBean {

    @Resource(lookup = "jms/QueueConnectionFactory")
    private ConnectionFactory queueConnectionFactory;

    @Resource(lookup = "jms/HelloQueue")
    private Queue helloQueue;

    @Resource(lookup = "jms/TopicConnectionFactory")
    private ConnectionFactory topicConnectionFactory;

    @Resource(lookup = "jms/HelloTopic")
    private Topic helloTopic;

    private int count = 0;

    @PostConstruct
    private void postConstruct() throws InterruptedException {
        createQueueProducer();
        for (int i = 0; i < 10; i++) {
            createQueueProducer();
            createTopicProducer();
            createTopicProducerByJndi();
            Thread.sleep(1000);
        }
    }

    private void createQueueProducer() {
        System.out.println("StartUpBean.createQueueProducer");

        TextMessage message;

        try (Connection connection = queueConnectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer producer = session.createProducer(helloQueue)) {

            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            String msg = "Hello Queue Startup " + count++;

            System.out.println("send queue msg = " + msg);

            message = session.createTextMessage(msg);

            producer.send(message);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void createTopicProducer() {
        System.out.println("StartUpBean.createTopicProducer");

        try {
            Connection con = topicConnectionFactory.createConnection();
            con.start();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //cannot get correct topic name.
            MessageProducer producer = session.createProducer(helloTopic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            String msg = "Hello Topic Startup " + count++;

            System.out.println("send topic msg = " + msg);

            TextMessage message = session.createTextMessage(msg);

            producer.send(message);

            session.close();
            con.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void createTopicProducerByJndi() {
        System.out.println("StartUpBean.createTopicProducer");

        try {
            // initialize context
            Context c = new InitialContext();

            TopicConnectionFactory qcf = (TopicConnectionFactory) c.lookup("jms/TopicConnectionFactory");

            //logger.debug("request queue name :" + requestQueueName);
            Topic topic = (Topic) c.lookup("jms/HelloTopic");


            Connection con = topicConnectionFactory.createConnection();
            con.start();
            Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //cannot get correct topic name.
            MessageProducer producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            String msg = "Hello Topic By JNDI Startup " + count++;

            System.out.println("send topic msg = " + msg);

            TextMessage message = session.createTextMessage(msg);

            producer.send(message);

            session.close();
            con.close();

        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }
}