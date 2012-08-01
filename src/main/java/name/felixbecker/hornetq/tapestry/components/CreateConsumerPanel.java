package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.services.HornetQConsumerService;

import org.apache.log4j.Logger;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class CreateConsumerPanel {
	
	private static Logger LOGGER = Logger.getLogger(CreateConsumerPanel.class);
	
	@Property
	@Inject
	HornetQConsumerService consumerService;
	
	
	@Property
	String consumerName;
	
	@Property
	String consumerQueue;

	@Property
	boolean logMessages;
	
	@Property
	boolean saveMessages;
	
	@Inject
	AlertManager alertManager;
	
	public void onSuccessFromCreateConsumerForm() {
		try {
			consumerService.createConsumer(consumerName, consumerQueue, logMessages, saveMessages);
		} catch(Exception e){
			alertManager.alert(Duration.SINGLE, Severity.ERROR, e.getMessage());
			LOGGER.error("Error creating consumer!", e);
		}
	}
}