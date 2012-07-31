package name.felixbecker.hornetq.services;

import java.util.Collection;

public interface HornetQConsumerService {
	public void createConsumer(String consumerName, String consumerQueue);
	public Collection<SampleConsumer> getConsumers();
}
