package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Graph;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.ShortestPath;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.util.FareCalculator;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse getPath(Long sourceId, Long targetId, int age) {
        Graph graph = new Graph();
        graph.addSections(new Sections(sectionDao.findAll()));
        ShortestPath shortestPath = graph.getShortestPath(getStationById(sourceId), getStationById(targetId));

        return PathResponse.from(shortestPath, FareCalculator.calculate(shortestPath.getDistance()));
    }

    private Station getStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철역이 존재하지 않습니다."));
    }
}
