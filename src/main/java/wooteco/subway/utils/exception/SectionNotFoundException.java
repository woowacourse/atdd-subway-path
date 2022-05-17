package wooteco.subway.utils.exception;

public class SectionNotFoundException extends RuntimeException {

    public SectionNotFoundException() {
        super("[ERROR] 지하철 경로가 존재하지 않습니다.");
    }
}
