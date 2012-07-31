package name.felixbecker.hornetq;

import name.felixbecker.hornetq.entities.SampleEntity;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
class SampleServiceImpl implements SampleService {

	private static final Logger LOGGER = Logger.getLogger(SampleServiceImpl.class);
	
	private final SessionFactory sessionFactory;

	@Autowired PlatformTransactionManager platformTransactionManager;
	@Autowired org.springframework.transaction.jta.JtaTransactionManager jtaTransactionManager;
	@Autowired
	public SampleServiceImpl(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void sendMessageAndPersistEntity(String message, String address, boolean sendMessage, boolean persistMessage, boolean durable) throws Exception {
		
		LOGGER.info("sending and persisting. sending: " + sendMessage + " - persisting: " + persistMessage);
		
		if(sendMessage){
			ServerLocator serverLocator = HornetQClient.createServerLocatorWithoutHA(new TransportConfiguration(InVMConnectorFactory.class.getName()));
			ClientSessionFactory hornetQSessionFactory = serverLocator.createSessionFactory();
			
			ClientSession clientSession = hornetQSessionFactory.createXASession();
			
			jtaTransactionManager.getTransactionManager().getTransaction().enlistResource(clientSession);
			ClientProducer producer = clientSession.createProducer(address);
			
			ClientMessage clientMessage = clientSession.createMessage(durable);
			clientMessage.putStringProperty("content", message);
			producer.send(clientMessage);

		}
		
		if(persistMessage){
			sessionFactory.getCurrentSession().save(new SampleEntity());
		}
		
	}

}

