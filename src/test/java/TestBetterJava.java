import net.imyeyu.betterjava.IO;
import net.imyeyu.betterjava.config.Config;
import net.imyeyu.betterjava.config.ConfigT;
import org.junit.Test;

import java.io.FileNotFoundException;

public class TestBetterJava {

	@Test
	public void testGetT() {
		ConfigT<Config> t = new ConfigT<>("test");
	}

	@Test
	public void jarFileToString() throws FileNotFoundException {
		System.out.println(IO.jarFileToString("test/test.txt"));
	}
}