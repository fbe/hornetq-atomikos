package name.felixbecker.hornetq.services;

import name.felixbecker.hornetq.entities.SampleEntity;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hornetq.api.core.SimpleString;
import org.hornetq.core.config.impl.FileConfiguration;
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
			hornetQInstance.createQueue(new SimpleString("foo"), new SimpleString("foo"), null, true, true);
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
	public void performHibernateSave() {
		LOGGER.info("Current session TX: " + sessionFactory.getCurrentSession().getTransaction());
		sessionFactory.getCurrentSession().save(new SampleEntity());
	}

	@Override
	public void performHibernateSessionLookup() {
		LOGGER.info("Current session TX: " + sessionFactory.getCurrentSession());
	}
	
	
}
