package name.felixbecker.hornetq;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.ClusterConnectionConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.core.server.JournalType;
import org.hornetq.core.server.cluster.ClusterConnection;

public class TestHornetQConfiguration extends ConfigurationImpl {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TestHornetQConfiguration.class);

	public static Configuration newInstance(){
		
		final String hostname;

		try {
		    hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			throw new RuntimeException("dude, you lost your localhost. thats not good..", e);
		}
		
		if("hornetq1".equals(hostname)){
			LOGGER.info("detected hostname hornetq1, creating configuration to be in cluster mode with hornetq2");
			return new TestHornetQConfiguration(); // TODO params
		} else if("hornetq2".equals(hostname)){
			LOGGER.info("detected hostname hornetq2, creating configuration to be in cluster mode with hornetq1");
			return new TestHornetQConfiguration(); // TODO params
		} else {
			LOGGER.info("not running on test server hornetq1 / hornetq2 (hostname based check), creating default configuration");
			return new TestHornetQConfiguration(); // TODO params
		}
		
		
	}
	
	private TestHornetQConfiguration(){
		
		setPersistenceEnabled(false);
		setSecurityEnabled(false);
		getAcceptorConfigurations().add(new TransportConfiguration(InVMAcceptorFactory.class.getName()));
		setClustered(true);
	
		// TransportConfiguration
		getAcceptorConfigurations();
		
		// AddressSettings
		getAddressesSettings();
		
		String random = UUID.randomUUID().toString();
		
		setJournalDirectory("/tmp/hornetq/" + random + "/journal/");
		setBindingsDirectory("/tmp/hornetq/" + random + "/bindings/");
		setPagingDirectory("/tmp/hornetq/" + random + "/pagings/");
		LOGGER.info("Using temp directory /tmp/hornetq/" + random + "/ for journal and bindings");
		
		// don't use AIO here because it requires libaio on Linux and we can't assume
		// that it's installed in production
		setJournalType(JournalType.NIO);
		
		setPersistenceEnabled(true); // TODO toggle me

		
		// netty 5446 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(TransportConstants.PORT_PROP_NAME, 5446);
		TransportConfiguration acceptorConfig = new TransportConfiguration(NettyAcceptorFactory.class.getName(), params);
		getAcceptorConfigurations().add(acceptorConfig);
		getAcceptorConfigurations().add(new TransportConfiguration(InVMAcceptorFactory.class.getName()));

		// unused till we have another node, then we need to know how to connect to it.
//		TransportConfiguration connectorConfig = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);
//		getConnectorConfigurations().put("netty", connectorConfig);
		
		LOGGER.info("Connectors: ");
		for (TransportConfiguration acceptorConfiguration : getAcceptorConfigurations()) {
			LOGGER.info("connectorConfiguration " + acceptorConfiguration.getName() + ": " + acceptorConfiguration);
		}
	}
	
	private void initCluster(){
		/* cluster connectors */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(TransportConstants.PORT_PROP_NAME, 5446);
		params.put(TransportConstants.HOST_PROP_NAME, "remote-node");
		
		TransportConfiguration connectorToOtherClusterNode = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);
		
		getConnectorConfigurations().put("connectionToRemoteNode", connectorToOtherClusterNode);
		
		ClusterConnectionConfiguration clusterConnectionConfiguration = new ClusterConnectionConfiguration(
				"myCluster", "#", "what connector?", 1, true, true, 1, -1, new ArrayList<String>(), true);
				
		// TODO 
		/* 
		<cluster-connection name="my-cluster">
		   <address>jms</address>
		   <connector-ref>netty-connector</connector-ref>
		   <retry-interval>500</retry-interval>
		   <use-duplicate-detection>true</use-duplicate-detection>
		   <forward-when-no-consumers>true</forward-when-no-consumers>
		   <max-hops>1</max-hops>
		   <static-connectors>
		       <connector-ref>server0-connector</connector-ref>
		       <connector-ref>server1-connector</connector-ref>
		   </static-connectors>
		</cluster-connection>
		*/
		/* connectors */
		
	}
	
	public String getHostname(){
		try {
		    return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			throw new RuntimeException("dude, you lost your localhost. thats not good..", e);
		}
	}

}
