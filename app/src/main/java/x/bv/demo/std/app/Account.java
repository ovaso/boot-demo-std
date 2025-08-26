package x.bv.demo.std.app;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class Account {
	@NotNull
	@Min(10000)
	private Integer serialNumber;
	@NotBlank
	@NotNull
	@Length(min = 8, max = 16)
	@Pattern(regexp = "[a-zA-Z0-9_]*")
	private String account;
}
