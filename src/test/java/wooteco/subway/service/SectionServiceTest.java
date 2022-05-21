package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.LineEntity;
import wooteco.subway.domain.section.SectionEntity;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.repository.SectionRepository;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("/truncate.sql")
public class SectionServiceTest {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final SectionRepository sectionRepository;
    private final SectionService sectionService;

    private Station 강남역;
    private Station 선릉역;
    private Station 잠실역;
    private Station 사당역;

    @Autowired
    SectionServiceTest(DataSource dataSource) {
        stationDao = new StationDao(dataSource);
        sectionDao = new SectionDao(dataSource);
        lineDao = new LineDao(dataSource);
        sectionRepository = new SectionRepository(stationDao, sectionDao);
        sectionService = new SectionService(sectionRepository);
    }

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        선릉역 = new Station("선릉역");
        잠실역 = new Station("잠실역");
        사당역 = new Station("사당역");

        stationDao.insert(강남역);
        stationDao.insert(선릉역);
        stationDao.insert(잠실역);
        stationDao.insert(사당역);

        lineDao.insert(new LineEntity("2호선", "blue", 1000));
        sectionDao.insert(new SectionEntity(1L, 1L, 2L, 10));
        sectionDao.insert(new SectionEntity(1L, 2L, 3L, 15));
    }

    @Test
    @DisplayName("구간을 하나 저장한다.")
    void saveSection() {
        sectionService.save(1L, new SectionRequest(3L, 4L, 10));

        assertThat(sectionDao.findByLineId(1L)).hasSize(3);
    }

    @Test
    @DisplayName("구간을 하나 삭제한다.")
    void deleteSection() {
        sectionService.delete(1L, 1L);

        assertThat(sectionDao.findByLineId(1L)).hasSize(1);
    }
}
