package x.bv.demo.std.ware.core.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import x.bv.demo.std.ware.core.web.exception.PlatformExceptionHandler;

@Configuration
@ConditionalOnClass(ConfigurableWebApplicationContext.class)
public class WebConfiguration {
	@Bean
	public PlatformExceptionHandler restExceptionHandler() {

		return new PlatformExceptionHandler();
	}
}
