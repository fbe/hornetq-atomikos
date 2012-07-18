package name.felixbecker.hornetq;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;

public class HornetQStartupListener implements ServletContextListener {

	private static final Logger LOGGER = Logger.getLogger(ServletContextListener.class);
	
	private HornetQServer hornetQInstance;
	
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.info("starting up hornetq");
		hornetQInstance = HornetQServers.newHornetQServer(TestHornetQConfiguration.newInstance());
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
