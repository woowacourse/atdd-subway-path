package wooteco.subway.admin.exception;

public class NoCriteriaExistsException extends IllegalArgumentException {
	public NoCriteriaExistsException() {
		super("존재하지 않는 최단 경로 조회 기준이에요.");
	}
}
