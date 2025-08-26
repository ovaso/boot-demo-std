package x.bv.demo.std.ware.core.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import x.bv.demo.std.ware.core.WareConfigurationProperties;

@Configuration
public class ValidatorConfig {

	private static final Logger log = LoggerFactory.getLogger(ValidatorConfig.class);

	@Bean
	public ValidationConfigurationCustomizer validationConfigurationCustomizer(WareConfigurationProperties properties) {
		log.info("配置 ValidationConfigurationCustomizer. 开启 fail_fast");
		return (configuration -> configuration.addProperty("hibernate.validator.fail_fast", properties.getValidatorFastFailed().toString()));
	}

	/**
	 * 配置使用 MethodValidationException 替代 ConstraintViolation 来解国际化问题
	 *
	 * @return
	 */
	@Bean
	public static MethodValidationPostProcessor validationPostProcessor() {

		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
		processor.setAdaptConstraintViolations(true);
		return processor;
	}
}
