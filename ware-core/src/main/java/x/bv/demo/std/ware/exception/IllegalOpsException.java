package x.bv.demo.std.ware.exception;

public class IllegalOpsException extends HttpRestException {
	public IllegalOpsException(int httpStatusCode, String message) {

		super(httpStatusCode, message);
	}
}
