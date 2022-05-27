package wooteco.subway.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.PathStrategy;
import wooteco.subway.domain.path.ShortestPathStrategy;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.StationResponse;

@Service
public class PathService {

    private static final String CALL_MYSELF_ERROR = "출발지와 도착지가 달라야 합니다.";

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse searchPaths(Long sourceId, Long targetId, int age) {
        validRequest(sourceId, targetId);
        List<Section> sections = sectionDao.findAll();

        PathStrategy pathStrategy = new ShortestPathStrategy(sections);
        Station source = stationDao.findById(sourceId);
        Station target = stationDao.findById(targetId);

        int distance = pathStrategy.calculateDistance(source, target);
        int extraFare = findMaxExtraFare(pathStrategy.findLineIds(source, target));
        FareCalculator fareCalculator = new FareCalculator(distance, extraFare, age);

        return new PathResponse(
                distance,
                fareCalculator.calculate(),
                generateStationResponses(pathStrategy.findPath(source, target))
        );
    }

    private int findMaxExtraFare(List<Long> lineIds) {
        Map<Long, Integer> extraFareMap = lineDao.findByIds(lineIds)
                .stream()
                .collect(Collectors.toMap(Line::getId, Line::getExtraFare));

        int maxExtraFare = 0;
        for (Long lineId : lineIds) {
            maxExtraFare = Math.max(maxExtraFare, extraFareMap.get(lineId));
        }
        return maxExtraFare;
    }

    private void validRequest(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException(CALL_MYSELF_ERROR);
        }
    }

    private List<StationResponse> generateStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
