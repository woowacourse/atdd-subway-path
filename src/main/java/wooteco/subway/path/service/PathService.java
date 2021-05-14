package wooteco.subway.path.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        List<Line> lines = lineService.findLines();

        initSubwayMap(subwayMap, lines);

        return getPathInformation(subwayMap, sourceStation, targetStation);
    }

    private void initSubwayMap(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap, List<Line> lines) {
        for (Line line : lines) {
            List<Station> stations = line.getStations();
            stations.forEach(subwayMap::addVertex);
            Sections sections = line.getSections();
            connectStation(subwayMap, sections);
        }
    }

    private void connectStation(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap, Sections sections) {
        for (Section section : sections.getSections()) {
            subwayMap.setEdgeWeight(subwayMap.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance());
        }
    }

    private PathResponse getPathInformation(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap, Station sourceStation, Station targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subwayMap);
        List<Station> shortestPath =
                dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
        int distance = (int) dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();
        return new PathResponse(StationResponse.listOf(shortestPath), distance);
    }
}
