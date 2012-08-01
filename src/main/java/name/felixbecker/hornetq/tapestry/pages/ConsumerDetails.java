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
	
	public Object onActivate(String consumerName){
		consumer = consumerService.getConsumers().get(consumerName);
		if(consumer == null){
			return Index.class;
		} else {
			return null;
		}
	}
	
	public long getRuntimeInMilliSeconds(){
		if(consumer.getLastMessageReceived() != null && consumer.getFirstMessageReceived() != null){
			return consumer.getLastMessageReceived().getTime() - consumer.getFirstMessageReceived().getTime();
		} else {
			return -1;
		}
	}
	
	public long getMessagesPerSecond(){
		if(getRuntimeInMilliSeconds() != -1){
			return consumer.getMessageCounter() / (getRuntimeInMilliSeconds() / 1000);
		}
		
		return 0;
	}
	
}
