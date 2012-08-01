package name.felixbecker.hornetq.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import name.felixbecker.hornetq.HornetQClientSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
class HornetQProducerServiceImpl implements HornetQProducerService {

	private final Collection<MessageProducer> producers = new ArrayList<MessageProducer>();
	
	@Autowired private HornetQClientSessionFactory sessionFactory;

	@Override
	public synchronized Collection<MessageProducer> getMessageProducers() {
		for(Iterator<MessageProducer> it = producers.iterator(); it.hasNext();){
			if(it.next().isShutdown()){
				it.remove();
			}
		}
		return producers;
	}

	@Override
	public void createProducer(String name, String address, String message, int num, long limit, boolean durable, int millisToSleepBetweenSending) {
		
		for(int i = 0; i < num; i++){
			MessageProducer producer = new MessageProducer(sessionFactory, name, address, message, limit, durable, millisToSleepBetweenSending);
			producers.add(producer);
			new Thread(producer).start();
		}
		
	}

}
