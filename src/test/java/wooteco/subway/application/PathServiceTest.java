package wooteco.subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.Infrastructure.*;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.response.PathResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class PathServiceTest {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final PathService pathService;

    public PathServiceTest() {
        stationDao = new MemoryStationDao();
        sectionDao = new MemorySectionDao();
        lineDao = new MemoryLineDao();
        pathService = new PathService(new FareCalculator(), sectionDao, stationDao);

        setUpStations();
        setUpSections();
    }

    private Station 선릉역;
    private Station 선정릉역;
    private Station 한티역;
    private Station 모란역;
    private Station 기흥역;
    private Station 강남역;

    void setUpStations() {
        this.선릉역 = new Station("선릉역");
        this.선정릉역 = new Station("선정릉역");
        this.한티역 = new Station("한티역");
        this.모란역 = new Station("모란역");
        this.기흥역 = new Station("기흥역");
        this.강남역 = new Station("강남역");

        long 선릉역_ID = stationDao.save(선릉역);
        long 선정릉역_ID = stationDao.save(선정릉역);
        long 한티역_ID = stationDao.save(한티역);
        long 모란역_ID = stationDao.save(모란역);
        long 기흥역_ID = stationDao.save(기흥역);
        long 강남역_ID = stationDao.save(강남역);

        this.선릉역.setId(선릉역_ID);
        this.선정릉역.setId(선정릉역_ID);
        this.한티역.setId(한티역_ID);
        this.모란역.setId(모란역_ID);
        this.기흥역.setId(기흥역_ID);
        this.강남역.setId(강남역_ID);
    }

    void setUpSections() {
        Line 노선_1 = new Line("노선_1", "red");
        Line 노선_2 = new Line("노선_2", "blue");
        Line 노선_3 = new Line("노선_3", "green");

        List<Section> sections = List.of(
                new Section(선릉역.getId(), 선정릉역.getId(), 50, 노선_1.getId()),
                new Section(선정릉역.getId(), 한티역.getId(), 8, 노선_1.getId()),
                new Section(한티역.getId(), 강남역.getId(), 20, 노선_1.getId()),
                new Section(선정릉역.getId(), 모란역.getId(), 6, 노선_2.getId()),
                new Section(기흥역.getId(), 모란역.getId(), 10, 노선_3.getId()),
                new Section(모란역.getId(), 강남역.getId(), 5, 노선_3.getId())
        );

        for (Section section : sections) {
            sectionDao.save(section);
        }
    }

    @DisplayName("경로를 조회한 결과가 순서에 맞게 출력되었는지 검증한다")
    @Test
    void findPath() {
        PathResponse result = pathService.findPath(기흥역.getId(), 한티역.getId());
        assertAll(
                () -> assertThat(result.getStations().get(0).getId()).isEqualTo(기흥역.getId()),
                () -> assertThat(result.getStations().get(1).getId()).isEqualTo(모란역.getId()),
                () -> assertThat(result.getStations().get(2).getId()).isEqualTo(선정릉역.getId()),
                () -> assertThat(result.getStations().get(3).getId()).isEqualTo(한티역.getId()),
                () -> assertThat(result.getDistance()).isEqualTo(24),
                () -> assertThat(result.getFare()).isEqualTo(1550)
        );
    }
}