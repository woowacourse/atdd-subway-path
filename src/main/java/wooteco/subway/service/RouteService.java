package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Route;
import wooteco.subway.domain.Section;

@Service
public class RouteService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public RouteService(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void createRoute(final Long sourceStationId, final Long targetStationId, final int age) {
        List<Line> lines = lineDao.findAll();
        Route route = new Route();
        for (Line line : lines) {
            route.addSections(sectionDao.findByLineId(line.getId()));
        }
    }
}
