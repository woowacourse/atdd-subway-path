package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.strategy.fare.FarePolicy;
import wooteco.subway.domain.strategy.path.PathFindStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.StationNotFoundException;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final PathFindStrategy pathStrategy;
    private final FarePolicy farePolicy;

    public PathService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao,
                       PathFindStrategy pathStrategy, FarePolicy farePolicy) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.pathStrategy = pathStrategy;
        this.farePolicy = farePolicy;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Station source = stationDao.findById(pathRequest.getSource())
                .orElseThrow(() -> new StationNotFoundException(pathRequest.getSource()));
        Station target = stationDao.findById(pathRequest.getTarget())
                .orElseThrow(() -> new StationNotFoundException(pathRequest.getTarget()));

        Sections sections = new Sections(sectionDao.findAll());
        Path path = pathStrategy.findPath(source, target, sections);
        List<Integer> linePrices = getLinePrices(path.getLineIds());

        return new PathResponse(toResponse(path.getStations()), path.getDistance(),
                farePolicy.calculateFare(path.getDistance(), linePrices, pathRequest.getAge()));
    }

    private List<StationResponse> toResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    private List<Integer> getLinePrices(List<Long> lineIds) {
        return lineDao.findLinePricesByIds(lineIds);
    }
}
