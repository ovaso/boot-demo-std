package x.bv.demo.std.ware.core.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import x.bv.demo.std.ware.core.web.exception.PlatformExceptionHandler;

/**
 * WEB MVC 相关配置
 */
@ConditionalOnWebApplication
public class WebConfiguration {
	@Bean
	public PlatformExceptionHandler restExceptionHandler() {

		return new PlatformExceptionHandler();
	}
}
