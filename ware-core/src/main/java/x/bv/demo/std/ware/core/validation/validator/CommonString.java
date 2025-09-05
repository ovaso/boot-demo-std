package x.bv.demo.std.ware.core.validation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 常规串校验器
 * <ol>
 *     <li>用户名</li>
 *     <li>密码</li>
 *     <li>...</li>
 * </ol>
 */
@NotEmpty
@NotNull
@Pattern(regexp = "^[a-zA-Z0-9\\-_]*$", message = "{ware.validator.pattern.string}")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {}) // 不需要自己写 Validator
public @interface CommonString {
	String message() default "手机号格式不正确";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
