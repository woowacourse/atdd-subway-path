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

    StationEntity one;
    StationEntity two;
    StationEntity three;
    LineEntity line1;
    LineEntity line2;
    SectionEntity section1;
    SectionEntity section2;
    SectionEntity section3;
    @Autowired
    private PathService pathService;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        one = stationDao.save(new StationEntity(null, "one"));
        two = stationDao.save(new StationEntity(null, "two"));
        three = stationDao.save(new StationEntity(null, "three"));
        line1 = lineDao.save(new LineEntity(null, "oneLine", "red"));
        line2 = lineDao.save(new LineEntity(null, "twoLine", "blue"));
        section1 = sectionDao.save(new SectionEntity(null, line1.getId(), one.getId(), two.getId(), 46));
        section2 = sectionDao.save(new SectionEntity(null, line2.getId(), two.getId(), three.getId(), 2));
        section3 = sectionDao.save(new SectionEntity(null, line1.getId(), one.getId(), three.getId(), 1000));
    }

    @Test
    @DisplayName("경로 조회한다")
    void getPath() {
        // given
        PathResponse pathResponse = pathService.getPath(three.getId(), one.getId());

        // then
        assertThat(pathResponse.getStations()).hasSize(3);
        assertThat(pathResponse.getDistance()).isEqualTo(48);
        assertThat(pathResponse.getFare()).isEqualTo(2050L);
    }
}
