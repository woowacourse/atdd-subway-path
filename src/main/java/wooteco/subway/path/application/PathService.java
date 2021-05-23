package wooteco.subway.path.application;

import java.util.ArrayList;
import java.util.List;
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

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Line> lines = lineService.findLines();

        List<Section> sectionsFromList = new ArrayList<>();
        for (Line line : lines) {
            sectionsFromList.addAll(line.getSections().getSections());
        }
        Sections sections = new Sections(sectionsFromList);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dPath = findDijkstraShortestPath(
            sections);

        List<StationResponse> stationResponses = findDijkstraShortestRoute(sourceStation,
            targetStation, dPath);
        int dDistance = findDijkstraShortestDistance(dPath, sourceStation,
            targetStation);

        return new PathResponse(stationResponses, dDistance);
    }

    private List<StationResponse> findDijkstraShortestRoute(Station sourceStation,
        Station targetStation,
        DijkstraShortestPath<Station, DefaultWeightedEdge> dPath) {
        List<StationResponse> stationResponses = new ArrayList<>();
        List<Station> shortestPaths = dPath.getPath(sourceStation, targetStation).getVertexList();

        for (Station station : shortestPaths) {
            stationResponses.add(new StationResponse(stationService.findIdByName(station.getName()),
                station.getName()));
        }
        return stationResponses;
    }

    private int findDijkstraShortestDistance(
        DijkstraShortestPath<Station, DefaultWeightedEdge> dPath,
        Station sourceStation, Station targetStation) {
        return (int) dPath.getPathWeight(sourceStation, targetStation);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> findDijkstraShortestPath(
        Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Section section : sections.getSections()) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
        }
        return new DijkstraShortestPath<>(graph);
    }

}
