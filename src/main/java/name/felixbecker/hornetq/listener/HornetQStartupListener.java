package name.felixbecker.hornetq.listener;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import name.felixbecker.hornetq.MessageProducingRunnable;
import name.felixbecker.hornetq.TestConstants;

import org.apache.log4j.Logger;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.MessageHandler;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.config.impl.FileConfiguration;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;

public class HornetQStartupListener implements ServletContextListener {

	private static final Logger LOGGER = Logger.getLogger(ServletContextListener.class);
	
	private HornetQServer hornetQInstance;

	private ClientSessionFactory clientSessionFactory;
	
	private ClientSession consumerSession;

	private Collection<MessageProducingRunnable> messageProducer = new ArrayList<MessageProducingRunnable>();

	private ServerLocator serverLocator;
	
	public void contextInitialized(ServletContextEvent sce) {
		
		LOGGER.info("loading hornetq configuration from hornetq-configuration.xml!");
		FileConfiguration fileConfiguration = new FileConfiguration();
		fileConfiguration.setConfigurationUrl("hornetq-configuration.xml");
		try {
			fileConfiguration.start();
		} catch (Exception e) {
			throw new RuntimeException("failed to load hornetq-configuration.xml", e);
		}
		
		LOGGER.info("starting up hornetq");
		hornetQInstance = HornetQServers.newHornetQServer(fileConfiguration);

		try {
			hornetQInstance.start();
			startMessageProducerAndConsumer();
		} catch (Exception e) {
			throw new RuntimeException("Failed to startup hornetq", e);
		}
		
		
	}


	private ClientSession addConsumer(ClientSessionFactory sessionFactory) throws Exception {

		final ClientSession session = sessionFactory.createSession();
        
	 	session.start();
        
	 	boolean queueExists = session.queueQuery(new SimpleString(TestConstants.TEST_QUEUE_NAME)).isExists();
		
	 	if(!queueExists){
	 		session.close();
	 		throw new RuntimeException("Queue " + TestConstants.TEST_QUEUE_NAME + " not found! Not creating consumer.");
		}
		
        final ClientConsumer messageConsumer = session.createConsumer(TestConstants.TEST_QUEUE_NAME);
        
        messageConsumer.setMessageHandler(new MessageHandler() {

			public void onMessage(ClientMessage message) {
					LOGGER.debug("received TextMessage: " + message.getStringProperty(TestConstants.TEST_MESSAGE_PROPERTY));
					try {
						message.acknowledge();
					} catch (HornetQException e) {
						LOGGER.error("acknowleged failed!", e);
					}
			}
			
        });
        
        return session;
	}


	private void startMessageProducerAndConsumer() throws Exception {
		
		serverLocator = HornetQClient.createServerLocatorWithoutHA(new TransportConfiguration(InVMConnectorFactory.class.getName()));
		// don't use global pools, they cause memory leaks.
		// but this doesn't seem to work too
		serverLocator.setUseGlobalPools(false);
		serverLocator.setScheduledThreadPoolMaxSize(50);
		serverLocator.setThreadPoolMaxSize(-1);  
		
		clientSessionFactory = serverLocator.createSessionFactory();

		MessageProducingRunnable messageProducingRunnable = new MessageProducingRunnable(clientSessionFactory);
		messageProducer.add(messageProducingRunnable);
		new Thread(messageProducingRunnable).start();
		
		
		consumerSession = addConsumer(clientSessionFactory);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		
		LOGGER.info("shutting down hornetq");
		
		try {
			
			LOGGER.info("shutting down message producer");
			for (MessageProducingRunnable runnable : messageProducer) {
				runnable.shutdown();
			}
			
			Thread.sleep(2000); // MPR have sleep times of 1000ms, wait here to ensure they are shutdown // TODO arno brauch ich das?
			
			consumerSession.stop();
			
			Thread.sleep(2000);
			
			clientSessionFactory.close();
			Thread.sleep(2000);
			
			serverLocator.close();
			Thread.sleep(2000);
			
			hornetQInstance.stop();
		} catch (Exception e) {
			throw new RuntimeException("Failed to stop hornetq", e);
		}
		
	}

}
