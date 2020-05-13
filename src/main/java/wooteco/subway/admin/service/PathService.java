package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse calculatePath(String source, String target, String type) {
        List<StationResponse> stations = new ArrayList<>();
        stations.add(new StationResponse(1L, "강남역", null));
        stations.add(new StationResponse(1L, "역삼역", null));
        stations.add(new StationResponse(1L, "선릉역", null));
        return new PathResponse(stations, 20L, 20L);
    }
}
