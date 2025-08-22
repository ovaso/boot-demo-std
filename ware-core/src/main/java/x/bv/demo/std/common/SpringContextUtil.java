package x.bv.demo.std.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {

		return applicationContext;
	}

	public <T> T getBean(Class<T> clazz) {

		return applicationContext.getBean(clazz);
	}
}
