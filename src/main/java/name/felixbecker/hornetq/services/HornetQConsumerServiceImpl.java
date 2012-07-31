package name.felixbecker.hornetq.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;

@Service
public class HornetQConsumerServiceImpl implements HornetQConsumerService {

	private Collection<SampleConsumer> consumers = new ArrayList<SampleConsumer>();
	
	@Override
	public synchronized void createConsumer(String consumerName, String consumerQueue) {
		consumers.add(new SampleConsumer(consumerName, consumerQueue));
	}

	@Override
	public Collection<SampleConsumer> getConsumers() {
		return consumers;
	}

}
