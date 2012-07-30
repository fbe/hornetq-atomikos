package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.services.HornetQService;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class QueueDisplayPanel {
	@Property
	@Inject
	HornetQService hornetQService;
	
	@Property
	String queue;
}