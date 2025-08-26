package x.bv.demo.std.ware.core.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@ConditionalOnProperty(
	prefix = "ware.async",
	name = "enable",
	havingValue = "true",
	matchIfMissing = false
)
public class AsyncConfig {

	private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

	public AsyncConfig() {

		log.info("外部配置类扫描, 开启异步配置");
	}

	@EnableAsync
	@Configuration
	public static class EnableAsyncConfig {
		private static final Logger log = LoggerFactory.getLogger(EnableAsyncConfig.class);

		public EnableAsyncConfig() {

			log.info("开启异步配置");
		}
	}
}
