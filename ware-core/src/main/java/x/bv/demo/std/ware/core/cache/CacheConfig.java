package x.bv.demo.std.ware.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 缓存相关配置
 */
@EnableCaching
@ConditionalOnClass(CacheManager.class)
public class CacheConfig {
	private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

	private final List<CacheConfigProvider> providers;

	public CacheConfig(@NonNull List<CacheConfigProvider> providers) {
		this.providers = providers;
		logger.info("共配置 {} 个缓存", providers.size());
	}
}
