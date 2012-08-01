package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.services.HornetQConsumerService;
import name.felixbecker.hornetq.services.MessageConsumer;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ConsumerDisplayPanel {
	@Property
	@Inject
	HornetQConsumerService hornetQConsumerService;
	
	@Property
	String currentConsumerKey;
	
	public MessageConsumer getCurrentConsumer(){
		return hornetQConsumerService.getConsumers().get(currentConsumerKey);
	}
}