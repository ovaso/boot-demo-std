package x.bv.demo.std.app;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import x.bv.demo.std.ware.core.validation.validator.CommonString;

@Data
public class Account {
	@NotNull
	@Min(10000)
	private Integer serialNumber;

	@Length(min = 8, max = 16)
	@CommonString
	private String account;
}
