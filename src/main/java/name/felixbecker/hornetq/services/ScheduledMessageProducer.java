package name.felixbecker.hornetq.services;

import name.felixbecker.hornetq.HornetQClientSessionFactory;

import org.hornetq.api.core.Message;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;

/**
 * @author Felix Becker
 * 
 * Message producing runnable, can be run by a thread pool / a normal thread.
 * 
 * contains shutdown method
 *
 */
public class ScheduledMessageProducer implements Runnable {

	volatile boolean shutdown;
	
	volatile int millisToSleepBetweenSending = 0;
	
	private final ClientSession clientSession; 
	private final ClientProducer producer;
	private final String message;
	private boolean durable = false;
	private final String name;
	
	public ScheduledMessageProducer(HornetQClientSessionFactory sessionFactory, String name, String address, String message, boolean durable, int millisToSleepBetweenSending){
		this.name = name;
		this.message = message;
		this.durable = durable;
		this.millisToSleepBetweenSending = millisToSleepBetweenSending;
		try {
			clientSession = sessionFactory.createSession(true, true);
			producer = clientSession.createProducer(address);
		} catch(Exception e){
			throw new RuntimeException(e); // TODO better exception handling
		}
	}
	
	@Override
	public void run() {
		try {
			while (true) {

				if (shutdown) {
					producer.close();
					clientSession.close();
					return;
				}

				final Message messageToSend = clientSession.createMessage(durable); // TODO
																			// durable
																			// configurable
				messageToSend.putStringProperty("content", message);
				producer.send(messageToSend);
				clientSession.commit();

				if (millisToSleepBetweenSending > 0) {
					Thread.sleep(millisToSleepBetweenSending);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void shutdown(){
		this.shutdown = true;
	}
	
	public String getName(){
		return name;
	}

}
