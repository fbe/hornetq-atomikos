package name.felixbecker.hornetq.services;

import java.util.Collection;


public interface HornetQProducerService {
	public void createProducer(String name, String address, String message, boolean durable, int millisToSleepBetweenSending);
	public Collection<ScheduledMessageProducer> getMessageProducers();
}