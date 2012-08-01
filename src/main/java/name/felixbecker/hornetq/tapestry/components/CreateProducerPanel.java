package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.services.HornetQProducerService;

import org.apache.log4j.Logger;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class CreateProducerPanel {
	
	private static Logger LOGGER = Logger.getLogger(CreateProducerPanel.class);
	
	@Property
	@Inject
	HornetQProducerService producerService;
	
	@Inject
	AlertManager alertManager;

	@Property
	String name;

	@Property
	String address;

	@Property
	String message;

	@Property
	boolean durable;

	@Property
	int millisToSleepBetweenSending;
	
	@Property
	int numbers;
	
	@Property
	long limit;
	
	
	public void onSuccessFromCreateProducerForm() {
		try {
			producerService.createProducer(name, address, message, numbers, limit, durable, millisToSleepBetweenSending);
		} catch(Exception e){
			alertManager.alert(Duration.SINGLE, Severity.ERROR, e.getMessage());
			LOGGER.error("Error creating producer", e);
		}
	}

	
}