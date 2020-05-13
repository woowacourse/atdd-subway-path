package wooteco.subway.admin.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

@Service
public class PathService {

    public PathResponse findShortestPath(String source, String target) {
        List<Station> stations = Arrays.asList(
                new Station(),
                new Station(),
                new Station(),
                new Station(),
                new Station()
        );
        PathResponse pathResponse = new PathResponse(StationResponse.listOf(stations), 40L, 40L);
        return pathResponse;
    }
}
