package name.felixbecker.hornetq.tapestry.components;

import name.felixbecker.hornetq.services.HornetQConsumerService;
import name.felixbecker.hornetq.services.SampleConsumer;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ConsumerDisplayPanel {
	@Property
	@Inject
	HornetQConsumerService hornetQConsumerService;
	
	@Property
	SampleConsumer consumer;
}