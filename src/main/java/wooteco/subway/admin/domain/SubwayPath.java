package wooteco.subway.admin.domain;

import java.util.List;

public interface SubwayPath {
    List<Station> stations();
    int distance();
    int duration();
}
