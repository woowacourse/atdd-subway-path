package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.section.Section;
import wooteco.subway.line.infrastructure.dao.LineDao;
import wooteco.subway.path.application.dto.PathResponseDto;
import wooteco.subway.path.application.dto.StationResponseDto;
import wooteco.subway.path.infrastructure.ShortestPath;
import wooteco.subway.station.domain.Station;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PathService {

    private final LineDao lineDao;

    public PathService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public PathResponseDto findPath(Long source, Long target) {
        List<Line> lines = lineDao.findAll();

        List<Section> sections = extractSections(lines);
        ShortestPath.Statistics statistics = getStatistics(source, target, lines, sections);
        List<StationResponseDto> stationResponses = getStationResponses(statistics);

        return new PathResponseDto(stationResponses, statistics.getDistance());
    }

    private List<Section> extractSections(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getSections().getSections().stream())
                .collect(toList());
    }

    private ShortestPath.Statistics getStatistics(Long source, Long target, List<Line> lines, List<Section> sections) {
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

    private List<StationResponseDto> getStationResponses(ShortestPath.Statistics statistics) {
        return statistics.getShortestPath().stream()
                .map(station -> new StationResponseDto(station.getId(), station.getName()))
                .collect(toList());
    }

}
