package x.bv.demo.std.ware.core.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步相关配置
 */
@ConditionalOnProperty(
	prefix = "wares.async",
	name = "enable",
	havingValue = "true"
)
@EnableAsync
public class AsyncConfig {

	private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

	public AsyncConfig() {

		log.info("外部配置类扫描, 开启异步配置");
	}
}
