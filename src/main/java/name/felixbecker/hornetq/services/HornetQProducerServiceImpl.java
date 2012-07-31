package name.felixbecker.hornetq.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;


@Service
class HornetQProducerServiceImpl implements HornetQProducerService {

	private final Collection<ScheduledMessageProducer> producers = new ArrayList<ScheduledMessageProducer>();
	
	@Override
	public Collection<ScheduledMessageProducer> getMessageProducers() {
		return producers;
	}

	@Override
	public void createProducer(String name, String address, String message,	boolean durable, int millisToSleepBetweenSending) {
		ScheduledMessageProducer producer = new ScheduledMessageProducer(name, address, message, durable, millisToSleepBetweenSending);
		producers.add(producer);
		new Thread(producer).start();
	}

}
