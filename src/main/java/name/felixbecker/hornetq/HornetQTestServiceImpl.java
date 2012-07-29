package name.felixbecker.hornetq;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
class HornetQTestServiceImpl implements HornetQTestService {

	
	private static final Logger LOGGER = Logger.getLogger(HornetQTestServiceImpl.class);
	
	private final SessionFactory sessionFactory;

//	private JmsTemplate jmsTemplate;
	
	
	@Autowired
	public HornetQTestServiceImpl(/*JmsTemplate jmsTemplate,*/ SessionFactory sessionFactory){
//		this.jmsTemplate = jmsTemplate;
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void sendMessage(String message, String queueName) {
//		jmsTemplate.convertAndSend(queueName, message);
		sessionFactory.getCurrentSession();
	}

}
