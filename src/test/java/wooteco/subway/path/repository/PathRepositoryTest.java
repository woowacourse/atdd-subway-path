package wooteco.subway.path.repository;

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
import wooteco.subway.path.domin.Path;
import wooteco.subway.path.domin.PathRepository;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PathRepositoryTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private PathRepository pathRepository;


    @DisplayName("최단 거리 경로를 리턴한다")
    @Test
    public void testGenerateShortestDistancePath() {
        Path path = pathRepository.generateShortestDistancePath(교대역, 양재역);
        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @BeforeEach
    public void setUp() {
        강남역 = stationDao.insert(new Station("강남역"));
        양재역 = stationDao.insert(new Station("양재역"));
        교대역 = stationDao.insert(new Station("교대역"));
        남부터미널역 = stationDao.insert(new Station("남부터미널역"));

        Line line = lineDao.insert(new Line("신분당선", "bg-red-600"));
        Section section = new Section(강남역, 양재역, 10);
        sectionDao.insert(line, section);

        line = lineDao.insert(new Line("이호선", "bg-red-600"));
        section = new Section(교대역, 강남역, 10);
        sectionDao.insert(line, section);

        line = lineDao.insert(new Line("삼호선", "bg-red-600"));
        section = new Section(남부터미널역, 양재역, 7);
        sectionDao.insert(line, section);

        section = new Section(교대역, 남부터미널역, 3);
        sectionDao.insert(line, section);
    }
}
