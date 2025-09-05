package x.bv.demo.std.ware.core.validation;

import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import x.bv.demo.std.ware.core.WareConfigurationProperties;

/**
 * 校验器相关配置
 */
@Configuration
@ConditionalOnClass(Validator.class)
public class ValidatorConfig {

	private static final Logger log = LoggerFactory.getLogger(ValidatorConfig.class);

	/**
	 * 配置全局校验器自定义配置
	 * @param properties 配置属性
	 * @return 校验器自定义配置
	 */
	@Bean
	public ValidationConfigurationCustomizer validationConfigurationCustomizer(WareConfigurationProperties properties) {

		log.info("配置 ValidationConfigurationCustomizer. 开启 fail_fast");
		return (configuration -> configuration.addProperty("hibernate.validator.fail_fast", properties.getValidatorFastFailed().toString()));
	}

	/**
	 * 配置使用 MethodValidationException 替代 ConstraintViolation 来解国际化问题
	 *
	 * @return 方法校验处理器
	 */
	@Bean
	public static MethodValidationPostProcessor validationPostProcessor() {

		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
		processor.setAdaptConstraintViolations(true);
		return processor;
	}
}
