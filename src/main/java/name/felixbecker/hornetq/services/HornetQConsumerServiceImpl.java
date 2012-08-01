package name.felixbecker.hornetq.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import name.felixbecker.hornetq.HornetQClientSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HornetQConsumerServiceImpl implements HornetQConsumerService {

	private final Map<String, MessageConsumer> consumers = new HashMap<String, MessageConsumer>();
	
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

	@Override
	public synchronized void restart() {
		for(Entry<String,MessageConsumer> consumer : consumers.entrySet()){
			consumer.getValue().destroy();
		}
		consumers.clear();
	}

}
