package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.response.PathResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        final List<Section> sections = sectionDao.findAll();

        final PathCalculator pathCalculator = new PathCalculator(sections);
        final List<Long> stationIds = pathCalculator.findShortestPath(sourceId, targetId);
        final double distance = pathCalculator.findShortestDistance(sourceId, targetId);
        final double fare = (new Fare()).calculate(distance);

        return PathResponse.from(convertStation(stationIds), distance, fare);
    }

    private List<Station> convertStation(final List<Long> stationIds) {
        return stationIds.stream()
                .map(stationDao::findById)
                .map(station -> station.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지하철역입니다.")))
                .collect(Collectors.toList());
    }
}
