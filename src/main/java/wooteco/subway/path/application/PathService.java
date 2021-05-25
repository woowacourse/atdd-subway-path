package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.ShortestDistanceStrategy;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        List<Station> stations = stationDao.findAll();
        List<Section> sections = sectionDao.findAll(stations);
        Path path = new Path(stations, sections, new ShortestDistanceStrategy());

        GraphPath<Station, DefaultWeightedEdge> shortestPath = path.calculateShortestPath(sourceId, targetId);
        List<Station> stationsInPath = shortestPath.getVertexList();
        int shortestDistance = (int) shortestPath.getWeight();

        List<StationResponse> stationResponses = stationsInPath.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, shortestDistance);
    }
}
