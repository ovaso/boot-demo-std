package x.bv.demo.std.ware.core.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * 聚合信息源
 * <br />
 * 组合各个模块下的消息模板, 使用 message 作为路径前缀
 */
public final class CompositeMessageSource
	extends AbstractMessageSource
	implements HierarchicalMessageSource {

	private static final Logger log = LoggerFactory.getLogger(CompositeMessageSource.class);
	private final List<ReloadableResourceBundleMessageSource> sources = new ArrayList<>();

	public CompositeMessageSource(List<ReloadableResourceBundleMessageSource> sources) {
		final Set<String> registeredKeys = new HashSet<>();
		for (ReloadableResourceBundleMessageSource source : sources) {
			addMessageSource(source, registeredKeys);
		}
	}

	public void addMessageSource(ReloadableResourceBundleMessageSource source, Set<String> registeredKeys) {

		if (source != null) {
			try {
				Enumeration<String> keys = getKeysFromMessageSource(source);
				if (Objects.isNull(keys)) return;
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					if (registeredKeys.contains(key)) {
						log.warn("Duplicate message key detected: {}", key);
					} else {
						registeredKeys.add(key);
					}
				}
			} catch (Exception e) {
				log.debug("Cannot extract keys from source: {}", source, e);
			}

			sources.add(source);
		}
	}

	private Enumeration<String> getKeysFromMessageSource(ReloadableResourceBundleMessageSource source) {

		try {
			List<String> basenameSet = new LinkedList<>(source.getBasenameSet());
			if (basenameSet.isEmpty()) {
				return Collections.emptyEnumeration();
			}
			Collections.sort(basenameSet);
			String basename = basenameSet.getFirst();
			Method method = ReflectionUtils.findMethod(source.getClass(), "getProperties", String.class);
			if (Objects.isNull(method)) return Collections.emptyEnumeration();;
			method.setAccessible(true);
			Object propertiesHolder = method.invoke(source, basename);

			Method getPropertiesMethodFromHolder = ReflectionUtils.findMethod(propertiesHolder.getClass(), "getProperties");
			if (Objects.isNull(getPropertiesMethodFromHolder)) return Collections.emptyEnumeration();
			getPropertiesMethodFromHolder.setAccessible(true);
			Properties properties = (Properties)getPropertiesMethodFromHolder.invoke(propertiesHolder);
			return Collections.enumeration(properties.keySet().stream().map(Object::toString).toList());
		} catch (InvocationTargetException | IllegalAccessException e) {
			return Collections.emptyEnumeration();
		}
	}

	@Override
	protected MessageFormat resolveCode(@NonNull String code, @NonNull Locale locale) {

		for (ReloadableResourceBundleMessageSource source : sources) {
			try {
				String msg = source.getMessage(code, null, null, locale);
				if (msg != null) return new MessageFormat(msg, locale);
			} catch (Exception ignored) {
			}
		}
		return null;
	}
}
