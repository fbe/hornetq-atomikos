package name.felixbecker.hornetq.services;

public interface HornetQConsumerService {
	public void createConsumer(String consumerName, String consumerQueue);
}
