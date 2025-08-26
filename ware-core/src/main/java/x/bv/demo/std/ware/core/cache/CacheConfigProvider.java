package x.bv.demo.std.ware.core.cache;

import java.util.List;

public interface CacheConfigProvider {
	String groupName();

	List<Object> configs();
}
