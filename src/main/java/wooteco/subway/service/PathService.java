package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.*;
import wooteco.subway.domain.strategy.ShortestPathStrategy;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.exception.DuplicatedSourceAndTargetException;
import wooteco.subway.exception.PathNotExistsException;
import wooteco.subway.exception.SectionNotExistException;
import java.util.List;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final ShortestPathStrategy strategy;

    public PathService(final StationDao stationDao, final SectionDao sectionDao, final ShortestPathStrategy strategy) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.strategy = strategy;
    }

    @Transactional(readOnly = true)
    public PathResponse createPath(final long sourceId, final long targetId, final int age) {
        validateDuplicatedSourceAndTarget(sourceId, targetId);
        final Station source = findStationById(sourceId);
        final Station target = findStationById(targetId);
        final List<Section> rawSections = sectionDao.findAll();
        final Sections sections = new Sections(rawSections);

        validateSectionExistByStationId(sections, sourceId);
        validateSectionExistByStationId(sections, targetId);
        final Path path = strategy.findPath(source, target, sections)
                .orElseThrow(PathNotExistsException::new);

        return PathResponse.of(path, getFare(path, age));
    }

    private void validateSectionExistByStationId(final Sections sections, final long stationId) {
        if (sections.doesNotContain(stationId)) {
            throw new SectionNotExistException("출발 또는 도착역에 해당하는 구간이 존재하지 않습니다.");
        }
    }

    private void validateDuplicatedSourceAndTarget(final long sourceId, final long targetId) {
        if (sourceId == targetId) {
            throw new DuplicatedSourceAndTargetException();
        }
    }

    private Station findStationById(final long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 지하철역 ID입니다."));
    }

    private int getFare(final Path path, final int age) {
        final AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.from(age);
        final int distance = path.getDistance();
        final int highestOverFareByLine = path.getLines().getHighestOverFare();
        return Fare.calculate(distance, highestOverFareByLine, ageDiscountPolicy);
    }
}
