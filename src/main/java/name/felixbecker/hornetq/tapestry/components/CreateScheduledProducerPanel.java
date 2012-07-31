package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.services.HornetQProducerService;

import org.apache.log4j.Logger;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class CreateScheduledProducerPanel {
	
	private static Logger LOGGER = Logger.getLogger(CreateScheduledProducerPanel.class);
	
	@Property
	@Inject
	HornetQProducerService producerService;
	
	
	@Property
	String producerAddress;
	
	@Property
	String producerName;
	
	@Inject
	AlertManager alertManager;
	
	public void onSuccessFromCreateScheduledProducerForm() {
		try {
			producerService.createProducer(producerName, producerAddress);
		} catch(Exception e){
			alertManager.alert(Duration.SINGLE, Severity.ERROR, e.getMessage());
			LOGGER.error("Error creating producer", e);
		}
	}
}