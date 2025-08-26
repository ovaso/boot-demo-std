package x.bv.demo.std.ware.common;

import lombok.Data;

@Data
public class RestResult<T> {
	private String message;
	private Integer code;
	private T data;

	public RestResult(String message, Integer code, T data) {

		this.message = message;
		this.code = code;
		this.data = data;
	}

	public RestResult(String message, Integer code) {

		this.message = message;
		this.code = code;
	}

	public RestResult(T data) {

		this.data = data;
	}
}
