import net.imyeyu.betterjava.BetterJava;
import net.imyeyu.betterjava.config.Config;
import net.imyeyu.betterjava.config.ConfigT;
import org.junit.Test;

public class TestBetterJava {

	@Test
	public void testGetT() throws Exception {
		ConfigT<Config> t = new ConfigT<>("test");
	}
}
