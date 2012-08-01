package name.felixbecker.hornetq.services;

import java.util.HashMap;
import java.util.Map;

import name.felixbecker.hornetq.HornetQClientSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HornetQConsumerServiceImpl implements HornetQConsumerService {

	private Map<String, MessageConsumer> consumers = new HashMap<String, MessageConsumer>();
	
	@Autowired HornetQClientSessionFactory clientSessionFactory;
	
	@Override
	public synchronized void createConsumer(String consumerName, String consumerQueue, boolean logMessages, boolean saveMessages) {
		if(consumers.containsKey(consumerName)){
			throw new RuntimeException("consumer with same name already exists!");
		} else {
			consumers.put(consumerName,new MessageConsumer(clientSessionFactory, consumerName, consumerQueue, logMessages, saveMessages));
		}
	}

	@Override
	public Map<String,MessageConsumer> getConsumers() {
		return consumers;
	}

}
