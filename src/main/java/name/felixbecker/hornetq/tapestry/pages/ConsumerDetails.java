package name.felixbecker.hornetq.tapestry.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import name.felixbecker.hornetq.services.HornetQConsumerService;
import name.felixbecker.hornetq.services.MessageConsumer;



public class ConsumerDetails {

	@Inject
	HornetQConsumerService consumerService;

	@Property
	MessageConsumer consumer;
	
	public void onActivate(String consumerName){
		consumer = consumerService.getConsumers().get(consumerName);
	}
	
}
