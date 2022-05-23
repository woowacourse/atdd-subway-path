package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.line.LineDao;
import wooteco.subway.dao.section.SectionDao;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.section.SectionSaveRequest;

@SpringBootTest
@Sql({"classpath:schema-truncate.sql", "classpath:init.sql"})
class SectionServiceTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionService sectionService;

    @Test
    @DisplayName("Section을 추가할 수 있다.")
    void save() {
        // given
        Line line = new Line(1L, "신분당선", "bg-red-600", 900);
        lineDao.save(line);
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));
        sectionDao.save(new Section(line, station1, station3, 10));

        // when
        int result = sectionService.save(1L, new SectionSaveRequest(station2.getId(), station3.getId(), 3));

        // then
        assertThat(result).isEqualTo(result);
    }

    @Test
    @DisplayName("Station을 받아 구간을 삭제할 수 있다.")
    void delete() {
        // given
        Line line = new Line(1L, "신분당선", "bg-red-600", 900);
        long lineId = lineDao.save(line);
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));
        sectionDao.save(new Section(line, station1, station2, 10));
        sectionDao.save(new Section(line, station2, station3, 10));

        // when
        sectionService.delete(lineId, station2.getId());

        // then
        assertThat(sectionDao.findAllByLineId(lineId)).hasSize(1)
                .extracting(Section::getUpStation, Section::getDownStation)
                .containsExactly(
                        tuple(station1, station3)
                );
    }
}
