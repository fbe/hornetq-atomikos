package name.felixbecker.hornetq.services;

import java.util.HashMap;
import java.util.Map;

import name.felixbecker.hornetq.HornetQClientSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
class HornetQProducerServiceImpl implements HornetQProducerService {

	private final Map<String, MessageProducer> producers = new HashMap<String, MessageProducer>();
	
	@Autowired private HornetQClientSessionFactory sessionFactory;

	@Override
	public synchronized Map<String, MessageProducer> getMessageProducers() {
		return producers;
	}

	@Override
	public void createProducer(String name, String address, String message, int num, long limit, boolean durable, int millisToSleepBetweenSending) {
		
		for(int i = 0; i < num; i++){
			String nameWithId = name + "-" + i;
			
			MessageProducer producer = new MessageProducer(sessionFactory, nameWithId, address, message, limit, durable, millisToSleepBetweenSending);
			if(producers.containsKey(nameWithId)){
				throw new RuntimeException("Producer " + nameWithId + " already exists!");
			}
			producers.put(nameWithId, producer);
			new Thread(producer).start();
		}
		
	}

}
