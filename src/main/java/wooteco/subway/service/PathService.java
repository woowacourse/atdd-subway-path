package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.*;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

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
        final Path graph = Path.of(new Sections(sectionDao.findAll()));
        final ShortestPath shortestPath = graph.getShortestPath(getStationById(pathRequest.getSource()), getStationById(pathRequest.getTarget()));

        final int fare = Fare.calculate(shortestPath.getSections());
        return PathResponse.from(shortestPath, Age.discountFare(pathRequest.getAge(), fare));
    }

    private Station getStationById(final Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철역이 존재하지 않습니다."));
    }
}
