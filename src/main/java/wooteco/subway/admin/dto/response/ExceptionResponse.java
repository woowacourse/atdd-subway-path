package wooteco.subway.admin.dto.response;

public class ExceptionResponse {
	private String message;

	private ExceptionResponse(String message) {
		this.message = message;
	}

	public static ExceptionResponse of(String message) {
		return new ExceptionResponse(message);
	}

	public String getMessage() {
		return message;
	}
}
