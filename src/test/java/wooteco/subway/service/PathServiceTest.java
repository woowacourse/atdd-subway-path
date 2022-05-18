package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;

@SpringBootTest
@Transactional
class PathServiceTest {

    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private PathService pathService;

    @DisplayName("시작역과 도착역을 통해서 Path를 생성한다.")
    @Test
    void createPath() {
        Line line2 = lineDao.save(new Line("2호선", "초록색"));
        Line line9 = lineDao.save(new Line("9호선", "금색"));
        Line bundangLine = lineDao.save(new Line("수인분당선", "노란색"));

        Station seolleung = stationDao.save(new Station("선릉역"));
        Station sportscomplex = stationDao.save(new Station("종합운동장역"));
        Station seonjeongneung = stationDao.save(new Station("선정릉역"));
        Station samjeon = stationDao.save(new Station("삼전역"));

        sectionDao.save(line2, new Section(seolleung, sportscomplex, 2));
        sectionDao.save(line9, new Section(sportscomplex, samjeon, 1));
        sectionDao.save(bundangLine, new Section(seolleung, seonjeongneung, 1));
        sectionDao.save(line9, new Section(seonjeongneung, sportscomplex, 3));

        PathResponse pathResponse = pathService.findShortestPath(
                new PathRequest(seolleung.getId(), samjeon.getId(), 20));

        assertThat(pathResponse).usingRecursiveComparison()
                .ignoringFields("stations")
                .isEqualTo(new PathResponse(null, 3, 1250));
        assertThat(pathResponse.getStations()).hasSize(3)
                .extracting("name")
                .containsExactly(seolleung.getName(), sportscomplex.getName(), samjeon.getName());
    }
    
}
