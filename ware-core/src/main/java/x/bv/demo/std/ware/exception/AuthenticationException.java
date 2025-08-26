package x.bv.demo.std.ware.exception;

/**
 * 401
 */
public class AuthenticationException extends IllegalOpsException {
	public AuthenticationException(String message) {

		super(401, message);
	}
}
