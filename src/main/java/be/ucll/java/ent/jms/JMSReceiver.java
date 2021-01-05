package be.ucll.java.ent.jms;

import be.ucll.java.ent.soap.model.v1.GetStudentsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.xml.transform.StringSource;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
/**
 * Java class that will respond to a JMS message that comes in over the Queue
 *
 * Just an example. All Queue Configuration and Queue Engine connectionfactory is missing/absent
 *
 */
public class JMSReceiver  {
    private static final Logger logger = LoggerFactory.getLogger(JMSReceiver.class);

    public void onMessage(Message message, Session session) throws JMSException {
        // We know/assume the Queue Payload type was set to 'TextMessage'
        TextMessage txtMessage = (TextMessage) message;
        logger.info("JMS Text Message received: " + txtMessage.getText());

        try {
            // And further we assume to receive XML messages so we need to unmarchal the assumed XML into a pojo
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setContextPath("be.ucll.java.ent.soap.model.v1"); // Base package of Jaxb

            GetStudentsRequest req = (GetStudentsRequest) marshaller.unmarshal(new StringSource(txtMessage.getText()));
            if (req != null) {
                // TODO Process the request ....
            }
        } catch (Exception e) {
            logger.error("Error unmarshalling XML String to Java Pojo", e);
        }

        // Notice the return type is void.
        // If a return message is needed to the calling system ANOTHER queue is needed
    }
}

