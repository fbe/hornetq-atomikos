package name.felixbecker.hornetq;

import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.hornetq.core.config.impl.FileConfiguration;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;

public class HornetQStartupListener implements ServletContextListener {

	private static final Logger LOGGER = Logger.getLogger(ServletContextListener.class);
	
	private HornetQServer hornetQInstance;
	
	public void contextInitialized(ServletContextEvent sce) {
		
		LOGGER.info("loading hornetq configuration from hornetq-configuration.xml!");
		FileConfiguration fileConfiguration = new FileConfiguration();
		fileConfiguration.setConfigurationUrl("hornetq-configuration.xml");

		try {
			fileConfiguration.start();
		} catch (Exception e) {
			throw new RuntimeException("failed to load hornetq-configuration.xml", e);
		}

		String random = UUID.randomUUID().toString();
		LOGGER.info("Overriding possibly configured journal, bindings and paging directory. Using temp directory /tmp/hornetq/" + random + "/");
		fileConfiguration.setJournalDirectory("/tmp/hornetq/" + random + "/journal/");
		fileConfiguration.setBindingsDirectory("/tmp/hornetq/" + random + "/bindings/");
		fileConfiguration.setPagingDirectory("/tmp/hornetq/" + random + "/pagings/");
		
		LOGGER.info("starting up hornetq");
		hornetQInstance = HornetQServers.newHornetQServer(fileConfiguration);
		
		try {
			hornetQInstance.start();
		} catch (Exception e) {
			throw new RuntimeException("Failed to startup hornetq", e);
		}
		
	}

	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.info("shutting down hornetq");
		try {
			hornetQInstance.stop();
		} catch (Exception e) {
			throw new RuntimeException("Failed to stop hornetq", e);
		}
	}

}
