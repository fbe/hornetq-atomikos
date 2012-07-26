package name.felixbecker.hornetq;

import javax.jms.Connection;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.atomikos.jms.AtomikosConnectionFactoryBean;

@Service
class HornetQTestServiceImpl implements HornetQTestService {

	
	private static final Logger LOGGER = Logger.getLogger(HornetQTestServiceImpl.class);
	
	private final SessionFactory sessionFactory;

	private JmsTemplate jmsTemplate;
	
	
	@Autowired
	public HornetQTestServiceImpl(JmsTemplate jmsTemplate, SessionFactory sessionFactory){
		this.jmsTemplate = jmsTemplate;
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void doHornetQStuffUhYeah() {
		try {
		jmsTemplate.convertAndSend("hallo welt!");
		sessionFactory.getCurrentSession();
		} catch(Exception e){
			throw new RuntimeException("bam, you failed! Your jms connection factory is broken!", e);
		}
	}

}
