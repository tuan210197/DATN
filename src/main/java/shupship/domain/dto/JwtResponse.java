package shupship.domain.dto;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final String userUid;

	public JwtResponse(String jwttoken, String userUid) {
		this.jwttoken = jwttoken;
		this.userUid = userUid;
	}

	public String getToken() {
		return this.jwttoken;
	}

	public String getUserUid() {
		return this.userUid;
	}
}