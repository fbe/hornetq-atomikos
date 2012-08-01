package name.felixbecker.hornetq.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import name.felixbecker.hornetq.HornetQClientSessionFactory;

import org.apache.log4j.Logger;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.MessageHandler;

public class MessageConsumer implements MessageHandler {

	private final Logger LOGGER = Logger.getLogger(MessageConsumer.class);
	
	
	private final String consumerName;
	
	private final ClientConsumer consumer;
	private final ClientSession clientSession;
	
	private AtomicLong messageCounter = new AtomicLong(0);
	private Date firstMessageReceived = null;
	private Date lastMessageReceived = null;
	
	volatile boolean logMessages;
	volatile boolean saveMessages;
	
	private Collection<ClientMessage> messageBox = new ArrayList<ClientMessage>();
	
	public Collection<ClientMessage> getMessageBox(){
		return messageBox;
	}
	
	public MessageConsumer(HornetQClientSessionFactory clientSessionFactory, String consumerName, String consumerQueue, boolean logMessages, boolean saveMessages) {
	
		this.logMessages = logMessages;
		this.saveMessages = saveMessages;
		
		this.consumerName = consumerName;
		try {
		
			ClientSession clientSession = clientSessionFactory.createSession(true, true);
			consumer = clientSession.createConsumer(consumerQueue);
			consumer.setMessageHandler(this);
			this.clientSession = clientSession;
			clientSession.start();

		} catch(Exception e){
			throw new RuntimeException("creating consumer failed! " + e.getMessage(), e);
		}

		
	}
	
	public void destroy(){
		try {
			consumer.close();
			clientSession.close();
		} catch (HornetQException e) {
			throw new RuntimeException("destroying consumer failed! " + e.getMessage(), e);
		}
		
	}

	@Override
	public void onMessage(ClientMessage message) {
		
		if(logMessages){
			LOGGER.info("Consumer "+ consumerName + "here! message: " + message.getStringProperty("content"));
		}
		
		if(saveMessages){
			messageBox.add(message);
		}
		
		if(firstMessageReceived == null){
			firstMessageReceived = new Date();
		}
		
		lastMessageReceived = new Date();
		
		try {
			message.acknowledge();
			clientSession.commit();
			messageCounter.incrementAndGet();
		} catch (HornetQException e) {
			LOGGER.error("exception receiving message!", e);
		}
	}

	public String getConsumerName() {
		return consumerName;
	}
	
	public synchronized long getMessageCounter(){
		return messageCounter.get();
	}

	public synchronized Date getFirstMessageReceived() {
		return firstMessageReceived;
	}

	public synchronized Date getLastMessageReceived() {
		return lastMessageReceived;
	}

}
