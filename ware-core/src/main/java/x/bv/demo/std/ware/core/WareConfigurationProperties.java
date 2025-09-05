package x.bv.demo.std.ware.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wares")
public class WareConfigurationProperties {
	private Boolean validatorFastFailed = true;
	private AsyncConfigProps async;

	@Data
	static class AsyncConfigProps {
		private Boolean enable = false;
	}
}
