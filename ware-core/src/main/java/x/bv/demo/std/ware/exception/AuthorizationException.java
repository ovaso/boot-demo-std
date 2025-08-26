package x.bv.demo.std.ware.exception;

/**
 * 403
 */
public class AuthorizationException extends IllegalOpsException {
	public AuthorizationException(String message) {

		super(403, message);
	}
}
