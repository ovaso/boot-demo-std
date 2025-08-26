package x.bv.demo.std.ware.exception;

/**
 * 409
 */
public class DataExistsException extends HttpRestException {
	public DataExistsException(String message) {

		super(409, message);
	}
}
