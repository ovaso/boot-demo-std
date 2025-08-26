package x.bv.demo.std.ware;

import org.springframework.context.annotation.Import;
import x.bv.demo.std.ware.core.WareConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(WareConfiguration.class)
public @interface EnableWares {
}
