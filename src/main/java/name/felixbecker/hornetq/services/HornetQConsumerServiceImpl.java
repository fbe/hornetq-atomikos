package name.felixbecker.hornetq.services;

import java.util.ArrayList;
import java.util.Collection;

import name.felixbecker.hornetq.HornetQClientSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HornetQConsumerServiceImpl implements HornetQConsumerService {

	private Collection<SampleConsumer> consumers = new ArrayList<SampleConsumer>();
	
	@Autowired HornetQClientSessionFactory clientSessionFactory;
	
	@Override
	public synchronized void createConsumer(String consumerName, String consumerQueue) {
		consumers.add(new SampleConsumer(clientSessionFactory, consumerName, consumerQueue));
	}

	@Override
	public Collection<SampleConsumer> getConsumers() {
		return consumers;
	}

}
