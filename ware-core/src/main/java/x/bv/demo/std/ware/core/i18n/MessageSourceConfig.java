package x.bv.demo.std.ware.core.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MessageSourceConfig {
	// @Bean
	// public MessageSource messageSource(List<MessageSource> messageSources) {
	//
	// 	// 默认消息
	// 	ReloadableResourceBundleMessageSource defaultSource = new ReloadableResourceBundleMessageSource();
	// 	defaultSource.setBasename("classpath:ware_message");
	// 	defaultSource.setDefaultEncoding("UTF-8");
	//
	// 	for (MessageSource messageSource : messageSources) {
	// 		if (messageSource instanceof HierarchicalMessageSource hms) {
	// 			hms.setParentMessageSource(defaultSource);
	// 		}
	// 	}
	//
	// 	return defaultSource;
	// }

	/**
	 * 扫描 messages 为前缀路径下的所有消息模板(ResourceBundle)
	 * @return 合并后的 CompositeMessageSource
	 * @throws Exception 异常
	 */
	@Bean
	public MessageSource messageSource() throws Exception {

		List<MessageSource> sources = new ArrayList<>();

		// 1. 默认消息
		ReloadableResourceBundleMessageSource defaultSource = new ReloadableResourceBundleMessageSource();
		defaultSource.setBasename("classpath:default_messages");
		defaultSource.setDefaultEncoding("UTF-8");
		sources.add(defaultSource);

		// 2. 扫描模块资源 bundle
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath*:messages/*.properties");

		for (Resource resource : resources) {
			String uri = resource.getURI().toString();
			String basename = uri.substring(0, uri.lastIndexOf(".properties"));
			ReloadableResourceBundleMessageSource moduleSource = new ReloadableResourceBundleMessageSource();
			moduleSource.setBasename(basename);
			moduleSource.setDefaultEncoding("UTF-8");
			sources.add(moduleSource);
		}

		// 3. 组合
		return new CompositeMessageSource(sources);
	}
}
