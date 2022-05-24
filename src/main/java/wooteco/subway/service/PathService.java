package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Subway;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.vo.Path;
import wooteco.subway.ui.dto.PathRequest;
import wooteco.subway.ui.dto.PathResponse;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<Line> lines = lineService.findAllLines();
        Subway subway = Subway.from(lines);
        Station source = stationService.findById(pathRequest.getSource());
        Station target = stationService.findById(pathRequest.getTarget());

        Path path = subway.findShortestPath(source, target);
        Fare fare = Fare.from(path.getDistance(), path.getLines(), pathRequest.getAge());
        return PathResponse.of(path, fare.getAmount());
    }
}
