package wooteco.subway.admin.acceptance;

import wooteco.subway.admin.dto.PathResponse;

public class PathAcceptanceTest extends AcceptanceTest{

    PathResponse findShortestPath(Long source, Long target, String type) {
        String path = "/api/paths?source=" + source + "&target=" + target + "&type=" + type;

        return super.get(path, PathResponse.class);
    }
}
