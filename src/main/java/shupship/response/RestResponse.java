package shupship.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class RestResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private Object data;
	private String errorCode;
	private String errorMessage;

	public RestResponse() {

		// Default is error
		this.status = "Lỗi ❤❤❤❤";
	}
}
