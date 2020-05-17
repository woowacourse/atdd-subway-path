package wooteco.subway.admin.dto;

public class ExceptionResponse {
	private final String message;

	private ExceptionResponse(String message) {
		this.message = message;
	}

	public static ExceptionResponse of(Exception e) {
		return new ExceptionResponse(e.getMessage());
	}

	public String getMessage() {
		return message;
	}
}
