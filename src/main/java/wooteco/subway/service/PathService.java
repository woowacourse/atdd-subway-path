package wooteco.subway.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.ShortestPath;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long upStationId, Long downStationId, int age) {
        validateStations(upStationId, downStationId);
        ShortestPath shortestPath = new ShortestPath(stationDao.findAll(), sectionDao.findAll());

        final List<Station> stations = findStations(upStationId, downStationId, shortestPath);
        final int shortestDistance = shortestPath.findShortestDistance(upStationId, downStationId);

        List<Integer> extraFares = findExtraFares(upStationId, downStationId, shortestPath);

        final Fare fare = Fare.of(shortestDistance, extraFares, age);

        return new PathResponse(stations, shortestDistance, fare.getValue());
    }

    private void validateStations(Long upStationId, Long downStationId) {
        findStation(upStationId);
        findStation(downStationId);

        if (Objects.equals(upStationId, downStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private Station findStation(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 존재하지 않습니다."));
    }

    private List<Station> findStations(Long upStationId, Long downStationId, ShortestPath shortestPath) {
        return shortestPath.findStationIds(upStationId, downStationId).stream()
                .map(this::findStation)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Integer> findExtraFares(Long upStationId, Long downStationId, ShortestPath shortestPath) {
        List<Long> passedLineIds = shortestPath.findPassedLineIds(upStationId, downStationId);
        return lineDao.findExtraFareByIds(passedLineIds);
    }
}
