package x.bv.demo.std.ware.core.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@ConditionalOnClass(ConfigurableWebApplicationContext.class)
public class WebConfiguration {
}
