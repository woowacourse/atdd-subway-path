package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.LineEntity;
import wooteco.subway.repository.entity.SectionEntity;
import wooteco.subway.repository.entity.StationEntity;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    StationEntity sinseolStationEntity;
    StationEntity sungsuStationEntity;
    StationEntity yongdapStationEntity;
    LineEntity line1;
    LineEntity line2;
    SectionEntity firstSection;
    SectionEntity secondSection;
    SectionEntity thirdSection;

    @BeforeEach
    void setUp() {
        sinseolStationEntity = stationDao.save(new StationEntity(null, "신설동역"));
        sungsuStationEntity = stationDao.save(new StationEntity(null, "성수역"));
        yongdapStationEntity = stationDao.save(new StationEntity(null, "용답역"));
        line1 = lineDao.save(new LineEntity(null, "1호선", "red", 0));
        line2 = lineDao.save(new LineEntity(null, "2호선", "blue", 0));
        firstSection = sectionDao.save(
                new SectionEntity(null, line1.getId(), sinseolStationEntity.getId(), sungsuStationEntity.getId(), 46));
        secondSection = sectionDao.save(
                new SectionEntity(null, line2.getId(), sungsuStationEntity.getId(), yongdapStationEntity.getId(), 2));
        thirdSection = sectionDao.save(
                new SectionEntity(null, line1.getId(), sinseolStationEntity.getId(), yongdapStationEntity.getId(),1000));
    }

    @Test
    @DisplayName("경로 조회한다")
    void getPath() throws Exception {
        // given
        PathResponse pathResponse = pathService.getPath(yongdapStationEntity.getId(), sinseolStationEntity.getId());

        // then
        assertThat(pathResponse.getStations()).hasSize(3);
        assertThat(pathResponse.getDistance()).isEqualTo(48);
        assertThat(pathResponse.getFare()).isEqualTo(2050L);
    }
}
