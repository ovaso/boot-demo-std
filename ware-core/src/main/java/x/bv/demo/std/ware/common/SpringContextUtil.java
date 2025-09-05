package x.bv.demo.std.ware.common;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

public class SpringContextUtil implements ApplicationContextAware {
	@Getter
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {

		SpringContextUtil.applicationContext = applicationContext;
	}

	public static <T> T getBean(@NonNull Class<T> clazz) {

		return applicationContext.getBean(clazz);
	}
}
