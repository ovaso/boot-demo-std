package x.bv.demo.std.ware.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConditionalOnClass(CacheManager.class)
public class CacheConfig {
	private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

	private final List<CacheConfigProvider> providers;

	public CacheConfig(List<CacheConfigProvider> providers) {

		logger.info("外部配置类被扫描注册");
		this.providers = providers;
	}

	@EnableCaching
	@Configuration
	public static class EnableCache{
		private static final Logger logger = LoggerFactory.getLogger(EnableCache.class);
		public EnableCache() {

			logger.info("内部配置类扫描注册开启缓存配置");
		}
	}
}
