package example;


import javax.annotation.Resource;
import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

//http://localhost:9080/send?msg=testja
@WebServlet("/send")
public class SimpleJMSServlet extends HttpServlet {

    @Resource(lookup = "jms/QueueConnectionFactory")
    ConnectionFactory jmsConnectionFactory;

    @Resource(lookup = "jms/HelloQueue")
    Queue helloQueue;

    @Resource(lookup = "jms/TopicConnectionFactory")
    ConnectionFactory topicConnectionFactory;

    @Resource(lookup = "jms/HelloTopic")
    Topic helloTopic;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(MediaType.TEXT_PLAIN);
        String msg = req.getParameter("msg" ) == null? "hello": req.getParameter("msg" );
        try {
            pushMessageToQueue(msg);
            pushMessageToTopic("topic " + msg);
        } catch (JMSException e) {
            resp.setStatus(502);
            resp.getWriter().println("Failed to Push " + msg + " to queue");
            resp.getWriter().println(e);
            e.printStackTrace();
        }
        resp.setStatus(200);
        resp.getWriter().println("Pushed " + msg + " to queue");
    }

    private void pushMessageToQueue(String msg) throws JMSException{
        Connection con = jmsConnectionFactory.createConnection();
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        TextMessage message = session.createTextMessage(msg);
        session.createProducer(helloQueue).send(message);
        session.close();
        con.close();
    }

    private void pushMessageToTopic(String msg) throws JMSException{
        Connection con = topicConnectionFactory.createConnection();
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        TextMessage message = session.createTextMessage(msg);
        session.createProducer(helloTopic).send(message);
        session.close();
        con.close();
    }

}
