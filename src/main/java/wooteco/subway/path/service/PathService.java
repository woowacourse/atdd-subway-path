package wooteco.subway.path.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        List<Line> lines = lineService.findLines();
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        
        initSubwayMap(subwayMap, lines);

        return getShortestPathInformation(subwayMap, sourceStation, targetStation);
    }

    private void initSubwayMap(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap, List<Line> lines) {
        for (Line line : lines) {
            List<Station> stations = line.getStations();
            stations.forEach(subwayMap::addVertex);
            Sections sections = line.getSections();
            connectStationsBySection(subwayMap, sections);
        }
    }

    private void connectStationsBySection(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap, Sections sections) {
        sections.getSections()
                .forEach(section -> subwayMap.setEdgeWeight(subwayMap.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));
    }

    private PathResponse getShortestPathInformation(WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap, Station sourceStation, Station targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subwayMap);
        List<StationResponse> shortestPath
                = StationResponse.listOf(dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList());
        return new PathResponse(shortestPath, (int)dijkstraShortestPath.getPathWeight(sourceStation, targetStation));
    }
}
