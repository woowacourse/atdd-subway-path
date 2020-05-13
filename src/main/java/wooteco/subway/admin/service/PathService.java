package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.StationResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class PathService {
    public PathResponse findPath(Long sourceId, Long targetId) {
        List<StationResponse> stations = Arrays.asList(mock(3L, "역삼역"), mock(4L, "강남역"), mock(5L, "교대역"), mock(6L, "잠원역"));
        return new PathResponse(stations, 30, 30);
    }

    private StationResponse mock(Long id, String name) {
        return new StationResponse(id, name, LocalDateTime.now());
    }
}
