package x.bv.demo.std.ware.core.web.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.lang.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 变换后的请求报文体
 */
public class ByteArrayCachedInputMessage implements HttpInputMessage {
	private final byte[] data;
	private final HttpHeaders headers;

	public ByteArrayCachedInputMessage(final byte[] data, final HttpHeaders headers) {

		if (Objects.isNull(data) || data.length == 0) {
			throw new IllegalArgumentException("不能包装空值");
		}
		this.data = data;
		if (Objects.isNull(headers) || headers.isEmpty()) {
			throw new IllegalArgumentException("不能包装空请求头");
		}
		this.headers = headers;
	}

	@NonNull
	@Override
	public InputStream getBody() throws IOException {

		return new ByteArrayInputStream(this.data);
	}

	@NonNull
	@Override
	public HttpHeaders getHeaders() {

		return this.headers;
	}
}
