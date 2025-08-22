package x.bv.demo.std.ware.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import x.bv.demo.std.ware.core.web.WebConfiguration;

@AutoConfiguration
@Import({
	WebConfiguration.class
})
public class WareConfiguration {
	private final Logger log = LoggerFactory.getLogger(WareConfiguration.class);
}
