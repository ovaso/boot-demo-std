package x.bv.demo.std.ware.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import x.bv.demo.std.ware.core.async.AsyncConfig;
import x.bv.demo.std.ware.core.cache.CacheConfig;
import x.bv.demo.std.ware.core.i18n.MessageSourceConfig;
import x.bv.demo.std.ware.core.validation.ValidatorConfig;
import x.bv.demo.std.ware.core.web.WebConfiguration;

@Configuration
@Import({
	AsyncConfig.class,
	CacheConfig.class,
	ValidatorConfig.class,
	WebConfiguration.class,
	MessageSourceConfig.class
})
@EnableConfigurationProperties(WareConfigurationProperties.class)
public class WareConfiguration {
	private final Logger log = LoggerFactory.getLogger(WareConfiguration.class);

	public WareConfiguration() {

		log.info("注册组件");
	}
}
