package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.SectionRequest;

@SpringBootTest
@Sql("classpath:truncate.sql")
public class SectionServiceTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private SectionService sectionService;

    Long 강남 = 1L;
    Long 선릉 = 2L;
    Long 잠실 = 3L;

    Station 강남역 = new Station(1L, "강남역");
    Station 선릉역 = new Station(2L, "선릉역");
    Station 잠실역 = new Station(3L, "잠실역");

    Line 지하철2호선 = new Line("2호선", "green");

    SectionRequest sectionRequest1 = new SectionRequest(강남, 선릉, 10);
    SectionRequest sectionRequest2 = new SectionRequest(선릉, 잠실, 10);

    @BeforeEach
    void setUp() {
        stationDao.insert(강남역);
        stationDao.insert(선릉역);
        stationDao.insert(잠실역);

        lineDao.insert(지하철2호선);
        sectionDao.insert(sectionRequest1.toSection(1L));
    }

    @Test
    @DisplayName("구간을 하나 저장한다.")
    void saveSection() {
        sectionService.save(1L, sectionRequest2);

        assertThat(sectionDao.findByLineId(1L)).hasSize(2);
    }

    @Test
    @DisplayName("구간을 하나 삭제한다.")
    void deleteSection() {
        sectionService.save(1L, sectionRequest2);

        sectionService.delete(1L, 강남);

        assertThat(sectionDao.findByLineId(1L)).hasSize(1);
    }

}
