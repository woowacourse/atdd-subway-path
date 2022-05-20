package wooteco.subway.ui.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
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

    public PathResponse getPath(int source, int target) {
        List<Section> sections = sectionDao.findAll();
        PathCalculator pathCalculator = PathCalculator.from(sections);

        Station sourceStation = stationDao.findById((long)source)
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        Station targetStation = stationDao.findById((long)target)
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        List<Station> path = pathCalculator.calculateShortestPath(sourceStation, targetStation);

        double distance = pathCalculator.calculateShortestDistance(sourceStation, targetStation);

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.execute();

        return new PathResponse(StationResponse.of(path), distance, fare);
    }
}
