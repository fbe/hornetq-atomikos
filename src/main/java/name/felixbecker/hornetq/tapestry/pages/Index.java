package name.felixbecker.hornetq.tapestry.pages;

import name.felixbecker.hornetq.services.HornetQService;

import org.apache.log4j.Logger;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;


public class Index {

	private static final Logger LOGGER = Logger.getLogger(Index.class);
	
	@Inject
	HornetQService hornetQService;
	
	@Inject
	Messages messages;
	
	@Inject
	AlertManager alertManager;

	@InjectComponent
	Zone formZone;
	
	@Property
	String configName;
	
	private boolean stop;
	
	void onSelectedFromStopServer() {
		stop = true;
	}     
	void onSelectedFromStartServer() {
		stop = false;
	}
	
	void onActionFromSaveSampleEntityLink(){
		hornetQService.performHibernateSave();
	}
	
	void onActionFromLookupSessionLink(){
		hornetQService.performHibernateSessionLookup();
	}
	
	public boolean getHornetQInstanceRunning(){
		return hornetQService.isHornetQRunning();
	}
	
	Object onSuccessFromStartHornetQForm(){
		try {
			if(stop){
				hornetQService.stopServer();
			} else {
				hornetQService.startServer(configName);
			}
			return formZone.getBody();
			
		} catch (Exception e) {
			LOGGER.error("Error starting hornetq!", e);
			alertManager.alert(Duration.SINGLE, Severity.ERROR, e.getMessage());
			return null;
		}
	}
}
