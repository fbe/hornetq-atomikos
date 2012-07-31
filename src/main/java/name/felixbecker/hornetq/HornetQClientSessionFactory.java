package name.felixbecker.hornetq;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;

public class HornetQClientSessionFactory {
	
	private final ServerLocator serverLocator;
	private ClientSessionFactory clientSessionFactory = null;
	
	public HornetQClientSessionFactory() throws Exception{
		serverLocator = HornetQClient.createServerLocatorWithoutHA(new TransportConfiguration(InVMConnectorFactory.class.getName()));
	}
	
	public ClientSession createSession(){
		try {
			initializeSessionFactoryIfRequired();
			return clientSessionFactory.createSession();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* called by spring on shutdown */
	public void destroy(){
		
			if(clientSessionFactory != null){
				clientSessionFactory.close();
			}
		
		serverLocator.close();
	}

	public ClientSession createSession(boolean autoCommit, boolean autoAck) {
		try {
			initializeSessionFactoryIfRequired();
			return clientSessionFactory.createSession(autoCommit, autoAck);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// lazy init of session factory because hornetq can be started later
	private synchronized void initializeSessionFactoryIfRequired() throws Exception{
		if(clientSessionFactory == null) clientSessionFactory = serverLocator.createSessionFactory();
	}
}
