package wooteco.subway.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwaySectionsGraph;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.error.exception.NotFoundException;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId, int age) {
        Station source = getStation(sourceId);
        Station target = getStation(targetId);

        SubwaySectionsGraph subwaySectionsGraph = new SubwaySectionsGraph(sectionDao.findAll());

        Path path = subwaySectionsGraph.getShortestPath(source, target);
        return new PathResponse(convertToStationResponse(path.getStations()), path.getDistance(), path.calculateFare());
    }

    private Station getStation(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new NotFoundException(id + "의 지하철역은 존재하지 않습니다."));
    }

    private List<StationResponse> convertToStationResponse(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(toList());
    }
}
