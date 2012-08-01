package name.felixbecker.hornetq.services;

import java.util.Map;


public interface HornetQProducerService {
	public void createProducer(String name, String address, String message, int num, long limit, boolean durable, int millisToSleepBetweenSending);
	public Map<String, MessageProducer> getMessageProducers();
}