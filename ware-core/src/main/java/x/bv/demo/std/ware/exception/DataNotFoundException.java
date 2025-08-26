package x.bv.demo.std.ware.exception;

/**
 * 404
 */
public class DataNotFoundException extends HttpRestException {
	public DataNotFoundException(String message) {

		super(404, message);
	}
}
