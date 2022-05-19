package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class ServiceTestFixture {

    public static void deleteAllStation(StationDao stationDao) {
        List<Station> stations = stationDao.findAll();

        List<Long> stationIds = stations.stream()
            .map(Station::getId)
            .collect(Collectors.toList());

        for (Long stationId : stationIds) {
            stationDao.deleteById(stationId);
        }
    }

    public static void deleteAllLine(LineDao lineDao) {
        List<Line> lines = lineDao.findAll();

        List<Long> lineIds = lines.stream()
            .map(Line::getId)
            .collect(Collectors.toList());

        for (Long lineId : lineIds) {
            lineDao.deleteById(lineId);
        }
    }

    public static void deleteAllSection(SectionDao sectionDao) {
        List<Section> sections = sectionDao.findAll();

        List<Long> sectionIds = sections.stream()
            .map(Section::getId)
            .collect(Collectors.toList());

        for (Long sectionId : sectionIds) {
            sectionDao.deleteById(sectionId);
        }
    }
}
