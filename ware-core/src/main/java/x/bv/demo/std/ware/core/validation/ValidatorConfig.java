package x.bv.demo.std.ware.core.validation;

import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import x.bv.demo.std.ware.core.i18n.CompositeMessageSource;

import java.util.Objects;
import java.util.Properties;

@Configuration
public class ValidatorConfig {

	private static final Logger log = LoggerFactory.getLogger(ValidatorConfig.class);

	@Bean
	public ValidatorFactory bean(final CompositeMessageSource messageSource) {

		log.info("配置 ValidatorFactory. 开启 fail_fast");
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		// bean.setConfigurationInitializer(beanConfig -> beanConfig.addProperty("hibernate.validator.fail_fast", "true"));

		Properties props = new Properties();
		props.setProperty("hibernate.validator.fail_fast", "true");
		factoryBean.setValidationProperties(props);
		if (Objects.nonNull(messageSource)) {
			factoryBean.setValidationMessageSource(messageSource);
		}
		return factoryBean;
	}
}
