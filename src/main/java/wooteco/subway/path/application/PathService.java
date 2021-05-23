package wooteco.subway.path.application;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dao.SectionDao;
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
    private final SectionDao sectionDao;

    public PathService(StationService stationService,
        LineService lineService, SectionDao sectionDao) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.sectionDao = sectionDao;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Line> lines = lineService.findLines();

        Sections sections = new Sections();
        for (Line line : lines) {
            sections.addSections(line.getSections());
        }
        DijkstraShortestPath dPath = findDijkstraShortestPath(sections);

        List<StationResponse> stationResponses = findDijkstraShortestRoute(sourceStation,
            targetStation, dPath);
        int dDistance = findDijkstraShortestDistance(dPath, sourceStation,
            targetStation);

        return new PathResponse(stationResponses, dDistance);
    }

    private List<StationResponse> findDijkstraShortestRoute(Station sourceStation,
        Station targetStation,
        DijkstraShortestPath dPath) {
        List<StationResponse> stationResponses = new ArrayList<>();
        List<String> shortestPaths = dPath.getPath(sourceStation.getName(), targetStation.getName())
            .getVertexList();
        for (String shortestPathName : shortestPaths) {
            stationResponses.add(new StationResponse(stationService.findIdByName(shortestPathName),
                shortestPathName));
        }
        return stationResponses;
    }

    private int findDijkstraShortestDistance(DijkstraShortestPath dPath,
        Station sourceStation, Station targetStation) {
        return (int) dPath.getPathWeight(sourceStation.getName(), targetStation.getName());
    }

    private DijkstraShortestPath findDijkstraShortestPath(
        Sections sections) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Section section : sections.getSections()) {
            String upStationName = section.getUpStation().getName();
            String downStationName = section.getDownStation().getName();
            graph.addVertex(upStationName);
            graph.addVertex(downStationName);
            graph.setEdgeWeight(graph.addEdge(upStationName, downStationName),
                section.getDistance());
        }
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath;
    }

}
