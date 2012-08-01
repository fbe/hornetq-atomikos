package name.felixbecker.hornetq.services;

import java.util.Map;

public interface HornetQConsumerService {
	public void createConsumer(String consumerName, String consumerQueue, boolean logMessages, boolean saveMessages);
	public Map<String,MessageConsumer> getConsumers();
}
