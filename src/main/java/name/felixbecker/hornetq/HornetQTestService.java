package name.felixbecker.hornetq;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface HornetQTestService {
	public void doHornetQStuffUhYeah();
}
