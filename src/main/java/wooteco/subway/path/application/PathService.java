package wooteco.subway.path.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.infrastructure.ShortestPath;
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

        List<Section> sections = lines.stream()
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(toList());

        ShortestPath.Statistics statistics = getStatistics(source, target, lines, sections);

        return new PathResponse(
            StationResponse.listOf(statistics.getShortestPath()),
            statistics.getDistance()
        );
    }

    private ShortestPath.Statistics getStatistics(
        Long source,
        Long target,
        List<Line> lines,
        List<Section> sections
    ) {
        ShortestPath shortestPath = new ShortestPath(sections);

        return shortestPath.statistics(
            stationIdToStation(source, lines),
            stationIdToStation(target, lines)
        );
    }


    private Station stationIdToStation(Long id, List<Line> lines) {
        return lines.stream()
            .flatMap(line -> line.getStations().stream())
            .filter(station -> station.getId().equals(id))
            .findAny()
            .orElseThrow(IllegalStateException::new);
    }

}