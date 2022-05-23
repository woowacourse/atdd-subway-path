package wooteco.subway.ui.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse getPath(PathRequest pathRequest) {
        long sourceStationId = pathRequest.getSource();
        long targetStationId = pathRequest.getTarget();
        return getPath(sourceStationId, targetStationId);
    }

    private PathResponse getPath(long source, long target) {
        List<Section> sections = sectionDao.findAll();
        PathCalculator pathCalculator = PathCalculator.from(sections);

        Station sourceStation = stationDao.findById(source)
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        Station targetStation = stationDao.findById(target)
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        List<Station> path = pathCalculator.calculateShortestPath(sourceStation, targetStation);

        double distance = pathCalculator.calculateShortestDistance(sourceStation, targetStation);

        return new PathResponse(StationResponse.of(path), distance, Fare.of(distance));
    }
}
