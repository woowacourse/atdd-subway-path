package wooteco.subway.exception;

public class SectionNotFoundException extends NotFoundException {

    public SectionNotFoundException() {
        super("[ERROR] 지하철 경로가 존재하지 않습니다.");
    }
}
