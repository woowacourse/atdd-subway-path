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
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
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
        Path path = new Path(stationDao.findAll(), sectionDao.findAll());

        final List<Station> stations = findStations(upStationId, downStationId, path);
        final int shortestDistance = path.findShortestDistance(upStationId, downStationId);
        final List<Line> passedLines = findPassedLines(path, upStationId, downStationId);

        final Fare fare = Fare.of(shortestDistance, passedLines, age);

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

    private List<Station> findStations(Long upStationId, Long downStationId, Path path) {
        return path.findStationIds(upStationId, downStationId).stream()
                .map(this::findStation)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Line> findPassedLines(Path path, Long upStationId, Long downStationId) {
        final List<Long> passedLineIds = path.findPassedLineIds(upStationId, downStationId);

        return passedLineIds.stream()
                .map(lineId -> lineDao.findById(lineId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 노선을 찾을 수 없습니다")))
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }
}
