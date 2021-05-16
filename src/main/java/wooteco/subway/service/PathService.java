package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.exception.notFound.StationNotFoundException;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.PathResponse;
import wooteco.subway.web.dto.StationResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse getShortestPath(Long sourceStationId, Long targetStationId) {
        List<Station> stations = stationDao.findAll();
        List<Section> sections = sectionDao.findAll();
        Station sourceStation = findStation(stations, sourceStationId);
        Station targetStation = findStation(stations, targetStationId);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = initGraphSetting(
            stations, sections);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        List<Station> shortestPath
            = dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
        List<StationResponse> stationResponses = shortestPath.stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());
        double shortestDistance
            = dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
        return new PathResponse(stationResponses, (int) shortestDistance);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initGraphSetting(
        List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station);
        }

        for (Section section : sections) {
            Long upStationId = section.getUpStation().getId();
            Long downStationId = section.getDownStation().getId();

            graph.setEdgeWeight(graph.addEdge(
                findStation(stations, upStationId),
                findStation(stations, downStationId)),
                section.getDistance());
        }
        return graph;
    }

    private Station findStation(List<Station> stations, Long id) {
        return stations.stream()
            .filter(station -> station.getId().equals(id)).findAny()
            .orElseThrow(StationNotFoundException::new);
    }
}
