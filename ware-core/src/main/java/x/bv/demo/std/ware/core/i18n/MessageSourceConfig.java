package x.bv.demo.std.ware.core.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class MessageSourceConfig {

	@Bean
	public MessageSource messageSource() throws Exception {

		List<ReloadableResourceBundleMessageSource> sources = new ArrayList<>();

		ReloadableResourceBundleMessageSource defaultSource = new ReloadableResourceBundleMessageSource();
		defaultSource.setBasename("classpath:ware_message");
		defaultSource.setDefaultEncoding("UTF-8");
		sources.add(defaultSource);

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath*:messages/*.properties");
		final Set<String> supportedLocales = Arrays.stream(Locale.getAvailableLocales())
			.map(Locale::toString)
			.collect(Collectors.toUnmodifiableSet());

		Map<String, Collection<String>> bucketSeparatedMessages = new HashMap<>();
		for (Resource resource : resources) {
			String resourcePath = resource.getURI().toString();
			String basePath = resourcePath.substring(0, resourcePath.lastIndexOf(File.separator));
			String fileName = resourcePath.substring(resourcePath.lastIndexOf(File.separator) + 1);
			String baseName = fileName.substring(0, fileName.lastIndexOf(".properties"));
			Collection<String> bucket = bucketSeparatedMessages.computeIfAbsent(basePath, k -> new HashSet<>());

			bucket.add(stripLocale(baseName, supportedLocales));
		}
		StringBuilder names = new StringBuilder();
		bucketSeparatedMessages.forEach((path, baseNames) -> {
			for (String baseName : baseNames) {
				String base = path + File.separator + baseName;
				ReloadableResourceBundleMessageSource moduleSource = new ReloadableResourceBundleMessageSource();
				moduleSource.setBasename(base);
				moduleSource.setDefaultEncoding("UTF-8");
				sources.add(moduleSource);
				names.append("\t").append(base).append("\n");
			}
		});
		names.deleteCharAt(names.length() - 1);
		log.info("""
			加载以下国际化资源文件:
			{}""", names);
		return new CompositeMessageSource(sources);
	}

	public static String stripLocale(String filename, Set<String> supportedLocales) {

		for (String locale : supportedLocales) {
			if (filename.endsWith("." + locale)
				|| filename.endsWith("_" + locale)
				|| filename.endsWith("." + locale.toLowerCase())
				|| filename.endsWith("_" + locale.toLowerCase())) {

				return filename.substring(0, filename.length() - locale.length() - 1);
			}
		}
		return filename; // 没有匹配
	}
}
