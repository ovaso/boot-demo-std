package x.bv.demo.std.ware.exception;

public class InternalServerException extends HttpRestException {
	public InternalServerException() {

		super(500, "SERVER_INTERNAL_ERROR");
	}
}
