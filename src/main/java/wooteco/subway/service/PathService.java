package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.strategy.DijkstraStrategy;
import wooteco.subway.domain.strategy.ShortestPathStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.exception.DuplicatedSourceAndTargetException;

@Service
public class PathService {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public PathService(final StationDao stationDao, final LineDao lineDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse createPath(final PathRequest pathRequest) {
        validateDuplicatedSourceAndTarget(pathRequest.getSource(), pathRequest.getTarget());
        final Station source = stationDao.findById(pathRequest.getSource())
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 지하철역 ID입니다."));
        final Station target = stationDao.findById(pathRequest.getTarget())
            .orElseThrow(() -> new DataNotFoundException("존재하지 않는 지하철역 ID입니다."));
        final Sections sections = new Sections(sectionDao.findAll());

        final ShortestPathStrategy strategy = new DijkstraStrategy();
        final Path path = strategy.findPath(source, target, sections);

        List<Integer> extraFares = new ArrayList<>();
        for (Long lineId : path.getLineIds()) {
            lineDao.findById(lineId)
                .ifPresent(line -> extraFares.add(line.getExtraFare()));
        }
        int max = extraFares.stream()
            .max(Integer::compareTo)
            .orElseThrow();

        final Fare fare = Fare.from(path.getDistance(), max, pathRequest.getAge());

        return PathResponse.from(path, fare);
    }

    private void validateDuplicatedSourceAndTarget(final long sourceId, final long targetId) {
        if (sourceId == targetId) {
            throw new DuplicatedSourceAndTargetException("출발역과 도착역은 같을 수 없습니다.");
        }
    }
}
