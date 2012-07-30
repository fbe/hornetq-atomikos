package name.felixbecker.hornetq.services;

import java.util.List;

import org.hornetq.core.config.impl.FileConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface HornetQService {
	public void startServer(String configurationName) throws Exception;
	public void stopServer() throws Exception;
	public boolean isHornetQRunning();
	public FileConfiguration getActiveConfiguration();
	public List<String> getQueues();
	public void createPersistentQueue(String address, String queueName, boolean durable) throws Exception;
}
