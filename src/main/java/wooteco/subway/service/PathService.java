package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Graph;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.ShortestPath;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.util.FareCalculator;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public PathResponse getPath(final PathRequest pathRequest) {
        final Graph graph = new Graph();
        graph.addSections(new Sections(sectionDao.findAll()));
        final ShortestPath shortestPath = graph.getShortestPath(getStationById(pathRequest.getSource()), getStationById(pathRequest.getTarget()));

        return PathResponse.from(shortestPath, FareCalculator.calculate(shortestPath.getDistance()));
    }

    private Station getStationById(final Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철역이 존재하지 않습니다."));
    }
}
