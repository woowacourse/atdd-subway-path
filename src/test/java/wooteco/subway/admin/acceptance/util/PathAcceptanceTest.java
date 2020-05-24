package wooteco.subway.admin.acceptance.util;

import wooteco.subway.admin.dto.PathResponse;

public class PathAcceptanceTest extends AcceptanceTest{

    public PathResponse findShortestPath(Long source, Long target, String type) {
        String path = "/api/paths?source=" + source + "&target=" + target + "&type=" + type;

        return super.get(path, PathResponse.class);
    }
}
