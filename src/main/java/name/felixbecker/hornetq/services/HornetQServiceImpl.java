package name.felixbecker.hornetq.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.config.impl.FileConfiguration;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class HornetQServiceImpl implements HornetQService {
	
	@Autowired private SessionFactory sessionFactory;
	
	private static final Logger LOGGER = Logger.getLogger(HornetQServers.class); 
	
	// must be singleton, only one hornetq in the JVM allowed!
	private static HornetQServer hornetQInstance;

	private FileConfiguration fileConfiguration;
	
	// it's definitly a problem if the server startup fails so 
	// we won't handle any exceptions here, you have to deal with it.
	public synchronized void startServer(String configurationName) throws Exception {
		if(hornetQInstance == null){
			LOGGER.info("loading hornetq configuration from hornetq-configuration.xml!");
			
			fileConfiguration = new FileConfiguration();
			fileConfiguration.setConfigurationUrl(configurationName);

			try {
				fileConfiguration.start();
			} catch (Exception e) {
				throw new RuntimeException("failed to load configuration "+configurationName+" " + e.getMessage(), e);
			}
			
			LOGGER.info("starting up hornetq");
			hornetQInstance = HornetQServers.newHornetQServer(fileConfiguration);
			hornetQInstance.start();
		} else {
			throw new RuntimeException("HornetQ instance already exists!");
		}
	}
	
	public synchronized void stopServer() throws Exception {
		if(hornetQInstance != null){
			hornetQInstance.stop();
			hornetQInstance = null;
			fileConfiguration.stop();
			fileConfiguration = null;
		}
	}
	
	public boolean isHornetQRunning(){

		return hornetQInstance != null && hornetQInstance.isStarted();
	}
	
	public FileConfiguration getActiveConfiguration(){
		return fileConfiguration;
	}

	@Override
	public List<String> getQueues() {
		if(hornetQInstance != null){
			return Arrays.asList(hornetQInstance.getHornetQServerControl().getQueueNames());
		} else {
			return new ArrayList<String>();
		}
	}
	
	public void createPersistentQueue(String address, String queueName, boolean durable) throws Exception {
		ServerLocator serverLocator = HornetQClient.createServerLocatorWithoutHA(new TransportConfiguration(InVMConnectorFactory.class.getName()));
		ClientSessionFactory hornetQSessionFactory = serverLocator.createSessionFactory();
		ClientSession clientSession = hornetQSessionFactory.createSession(true, true);
		clientSession.createQueue(new SimpleString(address), new SimpleString(queueName), durable);
		clientSession.close();
		hornetQSessionFactory.close();
	}
	
}
