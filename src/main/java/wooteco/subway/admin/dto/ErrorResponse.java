package wooteco.subway.admin.dto;

/**
 *    예외 리스폰스 클래스입니다.
 *
 *    @author HyungJu An
 */
public class ErrorResponse {
	private final String errorMessage;

	ErrorResponse(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public static ErrorResponse of(final String message) {
		return new ErrorResponse(message);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
