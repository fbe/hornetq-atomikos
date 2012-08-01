package name.felixbecker.hornetq.services;

import java.util.Collection;


public interface HornetQProducerService {
	public void createProducer(String name, String address, String message, int num, long limit, boolean durable, int millisToSleepBetweenSending);
	public Collection<MessageProducer> getMessageProducers();
}