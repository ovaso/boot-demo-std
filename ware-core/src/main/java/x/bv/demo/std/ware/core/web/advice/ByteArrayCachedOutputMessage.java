package x.bv.demo.std.ware.core.web.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.lang.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ByteArrayCachedOutputMessage implements HttpOutputMessage {
	private final HttpHeaders headers = new HttpHeaders();
	private final ByteArrayOutputStream bodyStream = new ByteArrayOutputStream();

	@Override
	@NonNull
	public OutputStream getBody() {

		return bodyStream;
	}

	@Override
	@NonNull
	public HttpHeaders getHeaders() {

		return headers;
	}

	public byte[] getBodyAsBytes() {

		return bodyStream.toByteArray();
	}

	public String getBodyAsString(Charset charset) {

		return bodyStream.toString(charset);
	}
}
