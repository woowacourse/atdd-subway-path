package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@JdbcTest
class SectionDaoTest {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    @Autowired
    public SectionDaoTest(JdbcTemplate jdbcTemplate) {
        this.lineDao = new LineDao(jdbcTemplate);
        this.stationDao = new StationDao(jdbcTemplate);
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @DisplayName("구간을 저장한다.")
    @Test
    void 구간_저장() {
        Line line2 = generateLine("2호선", "bg-green-600", 0);
        Station 선릉역 = generateStation("선릉역");
        Station 잠실역 = generateStation("잠실역");
        Integer distance = 10;

        Section createdSection = sectionDao.save(new Section(line2, 선릉역, 잠실역, distance));

        assertAll(
                () -> assertThat(createdSection.getLine()).isEqualTo(line2),
                () -> assertThat(createdSection.getUpStation()).isEqualTo(선릉역),
                () -> assertThat(createdSection.getDownStation()).isEqualTo(잠실역),
                () -> assertThat(createdSection.getDistance()).isEqualTo(distance)
        );
    }

    @DisplayName("구간 조작 기능을 확인한다.")
    @TestFactory
    Stream<DynamicTest> dynamicTestFromLine() {
        Line line = generateLine("2호선", "bg-green-600", 0);
        Station 선릉역 = generateStation("선릉역");
        Station 잠실역 = generateStation("잠실역");
        int distance1 = 10;
        Station 신도림역 = generateStation("신도림역");
        Station 신대방역 = generateStation("신대방역");
        int distance2 = 7;

        return Stream.of(
                dynamicTest("두 개의 구간이 주어지면 모두 저장한다.", () -> {
                    sectionDao.saveAll(
                            List.of(new Section(line, 선릉역, 잠실역, distance1),
                                    new Section(line, 신도림역, 신대방역, distance2)));

                    List<Section> sections = sectionDao.findByLineId(line.getId());
                    assertThat(sections.size()).isEqualTo(2);
                }),

                dynamicTest("노선의 id를 활용하여 노선 별 구간을 조회한다.", () -> {
                    List<Section> sections = sectionDao.findByLineId(line.getId());

                    assertThat(sections.size()).isEqualTo(2);
                }),

                dynamicTest("노선의 id를 활용하여 노선 별 구간을 삭제한다.", () -> {
                    sectionDao.deleteByLineId(line.getId());

                    List<Section> sections = sectionDao.findByLineId(line.getId());
                    assertThat(sections.size()).isEqualTo(0);
                })
        );
    }

    @DisplayName("지하철역 리스트가 주어지면 해당 지하철역을 가진 구간 리스트를 반환한다.")
    @Test
    void 특정_지하철역을_가진_구간_리스트_조회() {
        Line line = generateLine("2호선", "bg-green-600", 0);
        Station 선릉역 = generateStation("선릉역");
        Station 잠실역 = generateStation("잠실역");
        sectionDao.save(new Section(line, 선릉역, 잠실역, 10));
        Station 신대방역 = generateStation("신대방역");
        sectionDao.save(new Section(line, 신대방역, 선릉역, 7));

        List<Section> sections = sectionDao.findAllIn(List.of(선릉역));

        assertThat(sections.size()).isEqualTo(2);
    }

    private Line generateLine(String name, String color, int extraFare) {
        return lineDao.save(new Line(name, color, extraFare));
    }

    private Station generateStation(String name) {
        return stationDao.save(new Station(name));
    }
}
