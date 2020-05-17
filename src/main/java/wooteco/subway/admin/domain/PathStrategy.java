package wooteco.subway.admin.domain;

import java.util.List;

import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;

public interface PathStrategy {
    PathResponse getPath(
        final List<Line> allLines,
        final List<Station> allStations,
        final PathRequest request
    );
}
