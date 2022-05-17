package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.strategy.DijkstraStrategy;
import wooteco.subway.domain.strategy.ShortestPathStrategy;
import wooteco.subway.exception.DataNotFoundException;
import java.util.List;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Path createPath(final long sourceId, final long targetId, final int age) {
        final Station source = stationDao.findById(sourceId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 지하철역 ID입니다."));
        final Station target = stationDao.findById(targetId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 지하철역 ID입니다."));
        final List<Section> rawSections = sectionDao.findAll();
        final Sections sections = new Sections(rawSections);
        final ShortestPathStrategy strategy = new DijkstraStrategy();

        return strategy.findPath(source, target, sections);
    }
}
