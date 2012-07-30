package name.felixbecker.hornetq;

import org.apache.log4j.Logger;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.MessageHandler;

public class BroadcastMessageConsumer implements MessageHandler {
	
	private Logger LOGGER = Logger.getLogger(BroadcastMessageConsumer.class);
	
	public void onMessage(ClientMessage message) {
		try {
			if(LOGGER.isDebugEnabled()){  
			    LOGGER.debug("received TextMessage: " + message.getStringProperty(TestConstants.TEST_MESSAGE_PROPERTY));
			}
			
			message.acknowledge();
		} catch (HornetQException e) {
			LOGGER.fatal("error acknowledging message! ", e);
		}
	}
}
