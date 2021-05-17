package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import wooteco.subway.path.dto.PathResponse;

@Service
public class PathFindService {
    public PathResponse findShortestPath(final Long source, final Long target) {
        PathResponse pathResponse = new PathResponse();
        return pathResponse;
    }
}
