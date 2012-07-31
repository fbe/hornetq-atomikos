package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.SampleService;

import org.apache.log4j.Logger;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class MessageSendPanel {
	
	private static Logger LOGGER = Logger.getLogger(MessageSendPanel.class);
	
	@Property
	@Persist
	String queueName;
	
	@Property
	@Persist
	String message;
	
	@Property
	boolean persistEntity = false;
	

	@Property
	boolean sendMessage = false;
	
	@Property
	boolean durable = false;
	
	@Inject SampleService hornetQTestService;

	@Inject
	AlertManager alertManager;

	
	void onSuccessFromSendMessageForm(){
		try {
			hornetQTestService.sendMessageAndPersistEntity(message, queueName, sendMessage, persistEntity, durable);
		} catch(Exception e){
			LOGGER.info("Sending failed", e);
			alertManager.alert(Duration.SINGLE, Severity.ERROR, e.getMessage());
		}
	}
	
}