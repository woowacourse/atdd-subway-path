package wooteco.subway.admin.dto;

public class ExceptionResponse {
	private String frontMessageKey;
	private String message;

	public ExceptionResponse(String frontMessageKey, String message) {
		this.frontMessageKey = frontMessageKey;
		this.message = message;
	}

	public String getFrontMessageKey() {
		return frontMessageKey;
	}

	public String getMessage() {
		return message;
	}
}
