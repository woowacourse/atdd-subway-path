package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.Fixture.강남역;
import static wooteco.subway.Fixture.봉은사역;
import static wooteco.subway.Fixture.삼성역;
import static wooteco.subway.Fixture.삼성중앙역;
import static wooteco.subway.Fixture.삼전역;
import static wooteco.subway.Fixture.선릉역;
import static wooteco.subway.Fixture.선정릉역;
import static wooteco.subway.Fixture.역삼역;
import static wooteco.subway.Fixture.종합운동장역;

import java.util.List;
import java.util.stream.Collectors;
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
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@SpringBootTest
@Transactional
class PathServiceTest {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final PathService pathService;

    Long 강남_id;
    Long 삼전_id;

    @Autowired
    public PathServiceTest(StationDao stationDao, LineDao lineDao, SectionDao sectionDao, PathService pathService) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.pathService = pathService;
    }

    @Test
    @DisplayName("시작역과 도착역을 통해서 Path을 생성할 수 있다.")
    void createPath() {
        // given
        createStationAndSection();

        // when
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(강남_id, 삼전_id, 20));

        // then
        assertThat(pathResponse).usingRecursiveComparison()
                .ignoringFields("stations")
                .isEqualTo(new PathResponse(null, 25, 1550));
        assertThat(pathResponse.getStations()).hasSize(6)
                .extracting("name")
                .containsExactly(강남역.getName(), 역삼역.getName(), 선릉역.getName(), 삼성역.getName(), 종합운동장역.getName(),
                        삼전역.getName());
    }

    private List<StationResponse> toDtoResponse(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    private void createStationAndSection() {
        강남_id = stationDao.save(강남역);
        Long 역삼_id = stationDao.save(역삼역);
        Long 선릉_id = stationDao.save(선릉역);
        Long 삼성_id = stationDao.save(삼성역);
        Long 종합운동장_id = stationDao.save(종합운동장역);
        Long 선정릉_id = stationDao.save(선정릉역);
        Long 삼성중앙_id = stationDao.save(삼성중앙역);
        Long 봉은사_id = stationDao.save(봉은사역);
        삼전_id = stationDao.save(삼전역);

        Long 이호선_id = lineDao.save(new Line("2호선", "초록색", 0));

        sectionDao.save(new Section(이호선_id, new Station(강남_id, "강남역"), new Station(역삼_id, "역삼역"), 5));
        sectionDao.save(new Section(이호선_id, new Station(역삼_id, "역삼역"), new Station(선릉_id, "선릉역"), 5));
        sectionDao.save(new Section(이호선_id, new Station(선릉_id, "선릉역"), new Station(삼성_id, "삼성역"), 5));
        sectionDao.save(new Section(이호선_id, new Station(삼성_id, "삼성역"), new Station(종합운동장_id, "종합운동장역"), 5));
        sectionDao.save(new Section(이호선_id, new Station(선릉_id, "선릉역"), new Station(선정릉_id, "선정릉역"), 15));
        sectionDao.save(new Section(이호선_id, new Station(선정릉_id, "선정릉역"), new Station(삼성중앙_id, "삼성중앙역"), 10));
        sectionDao.save(new Section(이호선_id, new Station(삼성중앙_id, "삼성중앙역"), new Station(봉은사_id, "봉은사역"), 15));
        sectionDao.save(new Section(이호선_id, new Station(봉은사_id, "봉은사역"), new Station(종합운동장_id, "종합운동장역"), 5));
        sectionDao.save(new Section(이호선_id, new Station(종합운동장_id, "종합운동장역"), new Station(삼전_id, "삼전역"), 5));
    }
}
