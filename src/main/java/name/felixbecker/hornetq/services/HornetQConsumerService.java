package name.felixbecker.hornetq.services;

import java.util.Collection;

public interface HornetQConsumerService {
	public void createConsumer(String consumerName, String consumerQueue, boolean logMessages, boolean saveMessages);
	public Collection<MessageConsumer> getConsumers();
}
