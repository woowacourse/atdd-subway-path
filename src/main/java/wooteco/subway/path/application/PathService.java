package wooteco.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.JgraphtPathFinder;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.ShortestPathFinder;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.exception.NotFoundStationException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestStationPath(Long fromStationId, Long toStationId) {
        List<Station> stations = stationService.findStations();
        List<Line> lines = lineService.findLines();
        List<Section> sections = findAllSections(lines);

        SubwayMap subwayMap = new SubwayMap(stations, sections);
        Station fromStation = findStationById(stations, fromStationId);
        Station toStation = findStationById(stations, toStationId);
        Path path = subwayMap.getShortestPath(fromStation, toStation);
        return new PathResponse(path);
    }

    private List<Section> findAllSections(List<Line> lines) {
        return lines.stream()
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(Collectors.toList());
    }

    private Station findStationById(List<Station> stations, Long stationId) {
        return stations.stream()
            .filter(station -> station.isEqualToId(stationId))
            .findAny()
            .orElseThrow(() -> new NotFoundStationException(stationId));
    }
}
