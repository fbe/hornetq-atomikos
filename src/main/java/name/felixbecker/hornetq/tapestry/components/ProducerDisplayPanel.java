package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.services.HornetQProducerService;
import name.felixbecker.hornetq.services.MessageProducer;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ProducerDisplayPanel {
	@Property
	@Inject
	HornetQProducerService hornetQProducerService;
	
	@Property
	String currentProducerKey;
	
	public MessageProducer getCurrentProducer(){
		return hornetQProducerService.getMessageProducers().get(currentProducerKey);
	}
}