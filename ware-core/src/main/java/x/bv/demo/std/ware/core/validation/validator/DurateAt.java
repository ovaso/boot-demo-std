package x.bv.demo.std.ware.core.validation.validator;

import java.time.temporal.ChronoUnit;

/**
 * 时间段
 */
public @interface DurateAt {
	String duration();

	int amount();

	ChronoUnit unit();

	Mode mode() default Mode.WITHIN;

	String message() default "{ware.validator.durateAt}";

	enum Mode {
		// 在这个时间之外, 即大于等于这个时间
		EXCEEDED,
		// 在这个时间之内, 即小于等于这个时间
		WITHIN
	}
}
