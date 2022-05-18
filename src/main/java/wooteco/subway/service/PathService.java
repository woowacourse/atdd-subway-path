package wooteco.subway.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwaySectionsGraph;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationService stationService;

    public PathService(SectionDao sectionDao, StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long sourceId, Long targetId, int age) {
        Station source = stationService.findById(sourceId).toStation();
        Station target = stationService.findById(targetId).toStation();

        SubwaySectionsGraph subwaySectionsGraph = new SubwaySectionsGraph(sectionDao.findAll());

        Path path = subwaySectionsGraph.getShortestPath(source, target);
        return new PathResponse(convertToStationResponse(path.getStations()), path.getDistance(), path.calculateFare());
    }

    private List<StationResponse> convertToStationResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(toList());
    }
}
