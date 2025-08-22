package x.bv.demo.std.ware.core.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 组合各个模块下的消息模板, 使用 message 作为路径前缀
 */
public final class CompositeMessageSource
	extends AbstractMessageSource
	implements HierarchicalMessageSource {

	private static final Logger log = LoggerFactory.getLogger(CompositeMessageSource.class);
	private final List<MessageSource> sources = new ArrayList<>();
	private final Set<String> registeredKeys = new HashSet<>();

	public CompositeMessageSource(List<MessageSource> sources) {

		for (MessageSource source : sources) {
			addMessageSource(source);
		}
	}

	/**
	 * 支持动态添加模块 MessageSource
	 */
	public void addMessageSource(MessageSource source) {

		if (source != null) {
			// 记录 key 冲突
			try {
				Enumeration<String> keys = getKeysFromMessageSource(source);
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

	/**
	 * 尝试提取 ResourceBundle 的 keys，非 ResourceBundleSource 会失败
	 */
	private Enumeration<String> getKeysFromMessageSource(MessageSource source) {

		if (source instanceof AbstractMessageSource ams) {
			// ResourceBundleMessageSource 或 ReloadableResourceBundleMessageSource 内部维护 bundle
			// 此处仅示例，具体可用反射提取 baseNames 或 bundle
		}
		// 默认返回空
		return Collections.emptyEnumeration();
	}

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {

		for (MessageSource source : sources) {
			try {
				String msg = source.getMessage(code, null, null, locale);
				if (msg != null) return new MessageFormat(msg, locale);
			} catch (Exception ignored) {
			}
		}
		return null;
	}
}
