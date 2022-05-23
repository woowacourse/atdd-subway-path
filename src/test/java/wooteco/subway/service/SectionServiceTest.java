package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dao.line.InmemoryLineDao;
import wooteco.subway.dao.section.InmemorySectionDao;
import wooteco.subway.dao.station.InmemoryStationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.section.SectionSaveRequest;

class SectionServiceTest {

    private final InmemoryLineDao lineDao = InmemoryLineDao.getInstance();
    private final InmemorySectionDao sectionDao = InmemorySectionDao.getInstance();
    private final InmemoryStationDao stationDao = InmemoryStationDao.getInstance();

    private final SectionService sectionService = new SectionService(lineDao, sectionDao, stationDao);

    @AfterEach
    void afterEach() {
        lineDao.clear();
        sectionDao.clear();
        stationDao.clear();
    }

    @Test
    @DisplayName("Section을 추가할 수 있다.")
    void save() {
        // given
        Line line = lineDao.findById(lineDao.save(new Line("신분당선", "bg-red-600", 100)));
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));
        sectionDao.save(new Section(line, station1, station3, 10));

        // when
        int result = sectionService.save(line.getId(), new SectionSaveRequest(station1.getId(), station2.getId(), 3));

        // then
        assertThat(result).isEqualTo(result);
    }

    @Test
    @DisplayName("Station을 받아 구간을 삭제할 수 있다.")
    void delete() {
        // given
        Line line = lineDao.findById(lineDao.save(new Line("신분당선", "bg-red-600", 100)));
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));
        sectionDao.save(new Section(line, station1, station2, 10));
        sectionDao.save(new Section(line, station2, station3, 10));

        // when
        sectionService.delete(line.getId(), station2.getId());

        // then
        assertThat(sectionDao.findAllByLineId(line.getId())).hasSize(1)
            .extracting(Section::getUpStation, Section::getDownStation)
            .containsExactly(
                    tuple(station1, station3)
            );
    }
}
