package wooteco.subway.domain;

public interface Graph {

    Path search(Long source, Long target);
}
