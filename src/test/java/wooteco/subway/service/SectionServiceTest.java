package wooteco.subway.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.entity.SectionEntity;
import wooteco.subway.entity.StationEntity;

@SpringBootTest
@Sql("classpath:schema.sql")
class SectionServiceTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionService sectionService;

    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Long newId;

    @Test
    @DisplayName("구간을 등록한다.")
    public void enrollSection() {
        // given
        setUpLineStation();
        // when
        final SectionRequest sectionRequest = new SectionRequest(downStationId, newId, 10);
        // then
        assertThatCode(() -> sectionService.enroll(lineId, sectionRequest))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    public void deleteSection() {
        // given
        setUpLineStation();
        // when
        final SectionRequest sectionRequest = new SectionRequest(downStationId, newId, 10);
        sectionService.enroll(lineId, sectionRequest);

        // then
        assertThatCode(() -> sectionService.delete(lineId, downStationId))
            .doesNotThrowAnyException();
    }

    void setUpLineStation() {
        final Station stationA = new Station("A역");
        final Station stationB = new Station("B역");
        final Station stationC = new Station("C역");

        upStationId = stationDao.save(StationEntity.from(stationA));
        downStationId = stationDao.save(StationEntity.from(stationB));
        newId = stationDao.save(StationEntity.from(stationC));

        lineId = lineDao.save(LineEntity.from(new Line("노선", "색깔")));

        sectionDao.save(SectionEntity.from(
            new Section(new Station(upStationId, "A역"), new Station(downStationId, "B역"), new Distance(10)), lineId));
    }

}