package wooteco.subway.path.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.infrastructure.PathFinder;
import wooteco.subway.path.infrastructure.SubwayMap;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final LineDao lineDao;

    public PathService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public PathResponse findPath(Long source, Long target) {
        List<Line> lines = lineDao.findAll();
        List<Section> sections = getSectionsFromLines(lines);

        SubwayMap subwayMap = new SubwayMap();
        PathFinder pathFinder = new PathFinder(subwayMap.getSubwayMap(sections));

        Station sourceStation = stationIdToStation(source, lines);
        Station targetStation = stationIdToStation(target, lines);

        Path path = pathFinder.getShortestPath(sourceStation, targetStation);

        return new PathResponse(
            StationResponse.listOf(path.getStationPath()), path.getDistance()
        );
    }

    private List<Section> getSectionsFromLines(List<Line> lines) {
        return lines.stream()
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(toList());
    }

    private Station stationIdToStation(Long id, List<Line> lines) {
        return lines.stream()
            .flatMap(line -> line.getStations().stream())
            .filter(station -> station.getId().equals(id))
            .findAny()
            .orElseThrow(IllegalStateException::new);
    }

}