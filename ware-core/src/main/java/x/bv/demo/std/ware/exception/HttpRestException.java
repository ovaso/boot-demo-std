package x.bv.demo.std.ware.exception;

import lombok.Getter;

public class HttpRestException extends PlatformException{
	@Getter
	protected int httpStatusCode;
	@Getter
	protected String message;

	public HttpRestException(int httpStatusCode, String message) {
		this.httpStatusCode = httpStatusCode;
		this.message = message;
	}
}
