package wooteco.subway.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.service.path.PathFindable;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationService stationService;
    private final PathFindable pathFindable;

    public PathService(SectionDao sectionDao, StationService stationService, PathFindable pathFindable) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
        this.pathFindable = pathFindable;
    }

    public PathResponse findPath(Long sourceId, Long targetId, int age) {
        Station source = stationService.findById(sourceId).toStation();
        Station target = stationService.findById(targetId).toStation();

        Path path = pathFindable.findPath(sectionDao.findAll(), source, target);
        List<StationResponse> stationResponses = convertToStationResponse(path);
        int shortestDistance = path.getDistance();
        Lines lines = new Lines(getLines(path));
        Fare fare = new Fare(shortestDistance, age, lines.getMaxExtraFare());

        return new PathResponse(stationResponses, shortestDistance, fare.calculateFare());
    }

    private List<StationResponse> convertToStationResponse(Path shortestPath) {
        return shortestPath.getStations()
                .stream()
                .map(StationResponse::new)
                .collect(toList());
    }

    private List<Line> getLines(Path path) {
        return path.getSections()
                .stream()
                .map(Section::getLine)
                .collect(toList());
    }
}
