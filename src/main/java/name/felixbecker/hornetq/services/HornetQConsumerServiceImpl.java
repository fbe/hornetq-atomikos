package name.felixbecker.hornetq.services;

import java.util.ArrayList;
import java.util.Collection;

import name.felixbecker.hornetq.HornetQClientSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HornetQConsumerServiceImpl implements HornetQConsumerService {

	private Collection<MessageConsumer> consumers = new ArrayList<MessageConsumer>();
	
	@Autowired HornetQClientSessionFactory clientSessionFactory;
	
	@Override
	public synchronized void createConsumer(String consumerName, String consumerQueue, boolean logMessages, boolean saveMessages) {
		consumers.add(new MessageConsumer(clientSessionFactory, consumerName, consumerQueue, logMessages, saveMessages));
	}

	@Override
	public Collection<MessageConsumer> getConsumers() {
		return consumers;
	}

}
