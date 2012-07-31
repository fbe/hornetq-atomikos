package name.felixbecker.hornetq;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SampleService {
	public void sendMessageAndPersistEntity(String message, String address, boolean sendMessage, boolean persistEntity) throws Exception;
}
