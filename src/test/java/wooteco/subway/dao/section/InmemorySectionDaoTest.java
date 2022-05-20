package wooteco.subway.dao.section;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dao.line.InmemoryLineDao;
import wooteco.subway.dao.station.InmemoryStationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

class InmemorySectionDaoTest {

    private final InmemorySectionDao sectionDao = InmemorySectionDao.getInstance();
    private final InmemoryStationDao stationDao = InmemoryStationDao.getInstance();
    private final InmemoryLineDao lineDao = InmemoryLineDao.getInstance();

    @AfterEach
    void afterEach() {
        sectionDao.clear();
        stationDao.clear();
        lineDao.clear();
    }

    @Test
    @DisplayName("Section 을 저장할 수 있다.")
    void save() {
        // given
        Line line = new Line(1L, "name", "color", 100);
        Station upStation = new Station(1L, "오리");
        Station downStation = new Station(2L, "배카라");
        Section section = new Section(null, line, upStation, downStation, 1);

        // when
        long savedSectionId = sectionDao.save(section);

        // then
        assertThat(savedSectionId).isNotNull();
    }

    @Test
    @DisplayName("Line Id에 해당하는 Section을 조회할 수 있다.")
    void findAllByLineId() {
        Line line = new Line(1L, "name", "color", 100);
        Station station1 = new Station(1L, "오리");
        Station station2 = new Station(2L, "배카라");
        Station station3 = new Station(3L, "오카라");
        sectionDao.save(new Section(line, station1, station2, 2));
        sectionDao.save(new Section(line, station2, station3, 2));

        assertThat(sectionDao.findAllByLineId(line.getId())).hasSize(2);
    }

    @Test
    @DisplayName("모든 Section을 조회할 수 있다.")
    void findAll() {
        long id = lineDao.save(new Line("신분당선", "bg-red-600", 100));
        Line line = lineDao.findById(id);
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));

        sectionDao.save(new Section(line, station1, station2, 2));
        sectionDao.save(new Section(line, station2, station3, 2));

        assertThat(sectionDao.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("Section을 삭제할 수 있다.")
    void delete() {
        long lineId = lineDao.save(new Line("신분당선", "bg-red-600", 100));
        Line line = lineDao.findById(lineId);
        long stationId1 = stationDao.save(new Station("오리"));
        long stationId2 = stationDao.save(new Station("배카라"));
        long sectionId = sectionDao
                .save(new Section(line, stationDao.findById(stationId1), stationDao.findById(stationId2), 10));

        assertThat(sectionDao.delete(sectionId)).isEqualTo(1);
    }
}
