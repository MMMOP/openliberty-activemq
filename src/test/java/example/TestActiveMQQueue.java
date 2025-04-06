package example;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;

import javax.jms.*;

public class TestActiveMQQueue {
	private static String host = "tcp://localhost:61616";
	private static String queueName = "TEST.FOO"; //"HelloQueue"

	public static void main(String[] args) throws InterruptedException {
		TestActiveMQQueue test = new TestActiveMQQueue();
		test.testActiveMq();
	}

	@Test
	public void testActiveMq() throws InterruptedException {
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		Thread.sleep(1000);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
		Thread.sleep(1000);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldProducer(), false);
		Thread.sleep(1000);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldConsumer(), false);
		thread(new HelloWorldProducer(), false);
	}

	public static void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}

	public static class HelloWorldProducer implements Runnable {
		public void run() {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(host);

                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue(queueName);

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);

                // Create a messages
                String text = "Hello Queue! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);

                // Tell the producer to send the message
                System.out.println("Sent message: " + text + ": " + message.hashCode());
                producer.send(message);

                // Clean up
                session.close();
                connection.close();
            } catch (JMSException e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
		}
	}

	public static class HelloWorldConsumer implements Runnable, ExceptionListener {
		public void run() {
			try {
				// Create a ConnectionFactory
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(host);

				// Create a Connection
				Connection connection = connectionFactory.createConnection();
				connection.start();

				connection.setExceptionListener(this);

				// Create a Session
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				// Create the destination (Topic or Queue)

				Destination destination = session.createQueue(queueName);

				// Create a MessageConsumer from the Session to the Topic or Queue
				MessageConsumer consumer = session.createConsumer(destination);

				// Wait for a message
				Message message = consumer.receive(500);

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println("Received text: " + text);
				} else {
					System.out.println("Received message: " + message);
				}

				consumer.close();
				session.close();
				connection.close();
			} catch (Exception e) {
				System.out.println("Caught: " + e);
				e.printStackTrace();
			}
		}

		public synchronized void onException(JMSException ex) {
			System.out.println("JMS Exception occured.  Shutting down client.");
		}
    }

}
