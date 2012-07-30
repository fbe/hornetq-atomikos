package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.services.HornetQService;

import org.apache.log4j.Logger;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class CreateQueuePanel {
	
	private static Logger LOGGER = Logger.getLogger(CreateQueuePanel.class);
	
	@Property
	@Inject
	HornetQService hornetQService;
	
	
	@Property
	String createQueueName;
	
	@Property
	String createQueueAddress;
	
	@Property
	boolean createQueueDurable;
	
	@Inject
	AlertManager alertManager;
	
	public void onSuccessFromAddQueueForm() {
		try {
			hornetQService.createPersistentQueue(createQueueAddress, createQueueName, createQueueDurable);
		} catch(Exception e){
			alertManager.alert(Duration.SINGLE, Severity.ERROR, e.getMessage());
			LOGGER.error("Error creating queue", e);
		}
	}
}