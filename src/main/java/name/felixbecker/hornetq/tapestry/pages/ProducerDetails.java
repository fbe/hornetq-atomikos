package name.felixbecker.hornetq.tapestry.pages;

import java.util.Date;

import name.felixbecker.hornetq.services.HornetQProducerService;
import name.felixbecker.hornetq.services.MessageProducer;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;



public class ProducerDetails {

	@Inject
	HornetQProducerService producerService;

	@Property
	MessageProducer producer;
	
	public Object onActivate(String producerName){
		producer = producerService.getMessageProducers().get(producerName);
		if(producer == null){
			return Index.class;
		} else {
			return null;
		}
	}
	
	public long getRuntimeInMilliSeconds(){
		if(producer.getProducerStarted() != null){
			if(producer.getProducerFinished() != null){
				return producer.getProducerFinished().getTime() - producer.getProducerStarted().getTime();
			} else { // not finished yet
				final Date now = new Date();
				return now.getTime() - producer.getProducerStarted().getTime();
			}
		} 
		
		return -1;
	}
		
	
	public long getMessagesPerSecond(){
		if(getRuntimeInMilliSeconds() != -1){
			return producer.getMessageCounter() / (getRuntimeInMilliSeconds() / 1000);
		}
		
		return 0;
	}
	
}
