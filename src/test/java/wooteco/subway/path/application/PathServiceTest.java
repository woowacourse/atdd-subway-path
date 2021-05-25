package wooteco.subway.path.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PathServiceTest {
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private PathService pathService;

    private Station 강남역;
    private Station 판교역;
    private Station 정자역;
    private Line 신분당선;
    private Line 이호선;

    @BeforeEach
    void setup() {
        강남역 = stationDao.insert(new Station("강남역"));
        판교역 = stationDao.insert(new Station("판교역"));
        정자역 = stationDao.insert(new Station("정자역"));

        신분당선 = lineDao.insert(new Line("신분당선", "red lighten-1"));
        신분당선.addSection(new Section(강남역, 판교역, 10));
        신분당선.addSection(new Section(판교역, 정자역, 10));
        sectionDao.insertSections(신분당선);

        이호선 = lineDao.insert(new Line("2호선", "green lighten-1"));
        이호선.addSection(new Section(강남역, 정자역, 10));
        sectionDao.insertSections(이호선);
    }

    @AfterEach
    void cleanUp() {
        sectionDao.deleteByLineId(신분당선.getId());
        sectionDao.deleteByLineId(이호선.getId());
        lineDao.deleteById(이호선.getId());
        lineDao.deleteById(신분당선.getId());
        stationDao.deleteById(정자역.getId());
        stationDao.deleteById(판교역.getId());
        stationDao.deleteById(강남역.getId());
    }

    @DisplayName("경로 찾기 테스트")
    @Test
    void searchPath() {
        PathResponse pathResponse = pathService.searchPath(강남역.getId(), 정자역.getId());

        assertThat(pathResponse.getDistance()).isEqualTo(10);
        assertThat(pathResponse.getStations()).hasSize(2)
                .extracting("name")
                .containsExactly(강남역.getName(), 정자역.getName());
    }
}