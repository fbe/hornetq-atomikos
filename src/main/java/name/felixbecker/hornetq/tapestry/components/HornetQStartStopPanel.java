package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.HornetQClientSessionFactory;
import name.felixbecker.hornetq.services.HornetQConsumerService;
import name.felixbecker.hornetq.services.HornetQProducerService;
import name.felixbecker.hornetq.services.HornetQService;

import org.apache.log4j.Logger;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class HornetQStartStopPanel {
	
	private static Logger LOGGER = Logger.getLogger(HornetQStartStopPanel.class);
	
	private boolean stop;

	void onSelectedFromStopServer() {
		stop = true;
	}     
	void onSelectedFromStartServer() {
		stop = false;
	}
	
	
	@Inject 
	HornetQClientSessionFactory clientSessionFactory;

	@Inject
	HornetQProducerService hornetQProducerService;
	
	@Inject
	HornetQConsumerService hornetQConsumerService;
	
	@Inject
	@Property
	HornetQService hornetQService;
	
	@Inject
	AlertManager alertManager;
	
	@Property
	String configName;
	

	
	void onSuccessFromStartHornetQForm(){
		try {
			if(stop){
				hornetQConsumerService.restart();
				hornetQProducerService.restart();
				hornetQService.stopServer();
			} else {
				hornetQService.startServer(configName);
				clientSessionFactory.restartSessionFactory();
				
			}
			
		} catch (Exception e) {
			LOGGER.error("Error starting hornetq!", e);
			alertManager.alert(Duration.SINGLE, Severity.ERROR, e.getMessage());
		}
	}
}