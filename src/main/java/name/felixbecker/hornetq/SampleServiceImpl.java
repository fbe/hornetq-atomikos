package name.felixbecker.hornetq;

import name.felixbecker.hornetq.entities.SampleEntity;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.jms.core.JmsTemplate;

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
	public void sendMessageAndPersistEntity(String message, String queueName, boolean sendMessage, boolean persistMessage) throws Exception {
		
		if(sendMessage){
			ServerLocator serverLocator = HornetQClient.createServerLocatorWithoutHA(new TransportConfiguration(InVMConnectorFactory.class.getName()));
			ClientSessionFactory hornetQSessionFactory = serverLocator.createSessionFactory();
			
			ClientSession clientSession = hornetQSessionFactory.createXASession();
			
			jtaTransactionManager.getTransactionManager().getTransaction().enlistResource(clientSession);
			clientSession.createProducer(queueName);
		}
		
		if(persistMessage){
			sessionFactory.getCurrentSession().save(new SampleEntity());
		}
		
	}

}

