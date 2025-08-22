package x.bv.demo.std.ware.core;

import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = WareConfiguration.class)
@ExtendWith(InstancioExtension.class)
class WareConfigurationTest {
	@Test
	void testContext() {

	}
}
