package wooteco.subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.dao.entity.SectionEntity;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/testSchema.sql")
public class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @DisplayName("출발역 - 도착역 사이의 경로를 찾는다.")
    @Test
    void findPath() {
        //given
        Station 강남역 = stationDao.save(new Station("강남역"));
        Station 선릉역 = stationDao.save(new Station("선릉역"));
        Station 잠실역 = stationDao.save(new Station("잠실역"));
        Station 홍대역 = stationDao.save(new Station("홍대입구역"));

        LineEntity 일호선 = lineDao.save(new LineEntity("1호선", "green"));
        LineEntity 이호선 = lineDao.save(new LineEntity("2호선", "pink"));
        LineEntity 삼호선 = lineDao.save(new LineEntity("3호선", "blue"));

        sectionDao.save(new SectionEntity(일호선.getId(), 강남역.getId(), 선릉역.getId(), 3));
        sectionDao.save(new SectionEntity(삼호선.getId(), 강남역.getId(), 잠실역.getId(), 3));
        sectionDao.save(new SectionEntity(일호선.getId(), 선릉역.getId(), 홍대역.getId(), 2));
        sectionDao.save(new SectionEntity(이호선.getId(), 강남역.getId(), 홍대역.getId(), 10));
        sectionDao.save(new SectionEntity(삼호선.getId(), 잠실역.getId(), 홍대역.getId(), 5));

        //when
        PathResponse response = pathService.findPath(강남역.getId(), 홍대역.getId(), 22);

        //then
        assertAll(() -> {
            assertThat(response.getFare()).isEqualTo(1250);
            assertThat(response.getDistance()).isEqualTo(5);
            assertThat(response.getStations().get(0).getName()).isEqualTo("강남역");
            assertThat(response.getStations().get(1).getName()).isEqualTo("선릉역");
            assertThat(response.getStations().get(2).getName()).isEqualTo("홍대입구역");
        });
    }

    @DisplayName("여러 구간을 환승하는 경우")
    @Test
    void transfer() {
        Station 강남역 = stationDao.save(new Station("강남역"));
        Station 선릉역 = stationDao.save(new Station("선릉역"));
        Station 잠실역 = stationDao.save(new Station("잠실역"));
        Station 홍대역 = stationDao.save(new Station("홍대입구역"));
        Station 신촌역 = stationDao.save(new Station("신촌역"));
        Station 당산역 = stationDao.save(new Station("당산역"));
        Station 문래역 = stationDao.save(new Station("문래역"));
        Station 신림역 = stationDao.save(new Station("신림역"));

        LineEntity 일호선 = lineDao.save(new LineEntity("1호선", "yellow"));
        LineEntity 이호선 = lineDao.save(new LineEntity("2호선", "pink"));
        LineEntity 삼호선 = lineDao.save(new LineEntity("3호선", "blue"));
        LineEntity 사호선 = lineDao.save(new LineEntity("4호선", "purple"));

        sectionDao.save(new SectionEntity(일호선.getId(), 강남역.getId(), 선릉역.getId(), 10));
        sectionDao.save(new SectionEntity(이호선.getId(), 선릉역.getId(), 홍대역.getId(), 10));
        sectionDao.save(new SectionEntity(삼호선.getId(), 잠실역.getId(), 홍대역.getId(), 2));
        sectionDao.save(new SectionEntity(삼호선.getId(), 홍대역.getId(), 신촌역.getId(), 10));
        sectionDao.save(new SectionEntity(삼호선.getId(), 신촌역.getId(), 당산역.getId(), 5));
        sectionDao.save(new SectionEntity(삼호선.getId(), 당산역.getId(), 신림역.getId(), 5));
        sectionDao.save(new SectionEntity(사호선.getId(), 신촌역.getId(), 문래역.getId(), 5));
        sectionDao.save(new SectionEntity(사호선.getId(), 문래역.getId(), 신림역.getId(), 3));

        PathResponse response = pathService.findPath(강남역.getId(), 신림역.getId(), 22);

        //then
        assertAll(() -> {
            assertThat(response.getFare()).isEqualTo(1850);
            assertThat(response.getStations().size()).isEqualTo(6);
            assertThat(response.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                    .contains("강남역", "선릉역", "홍대입구역", "신촌역", "문래역", "신림역");
            assertThat(response.getDistance()).isEqualTo(38);
        });
    }

    @DisplayName("어린이 요금제 적용")
    @Test
    void fareForChild() {
        Station 강남역 = stationDao.save(new Station("강남역"));
        Station 선릉역 = stationDao.save(new Station("선릉역"));
        Station 잠실역 = stationDao.save(new Station("잠실역"));
        Station 홍대역 = stationDao.save(new Station("홍대입구역"));
        Station 신촌역 = stationDao.save(new Station("신촌역"));
        Station 당산역 = stationDao.save(new Station("당산역"));
        Station 문래역 = stationDao.save(new Station("문래역"));
        Station 신림역 = stationDao.save(new Station("신림역"));

        LineEntity 일호선 = lineDao.save(new LineEntity("1호선", "yellow"));
        LineEntity 이호선 = lineDao.save(new LineEntity("2호선", "pink"));
        LineEntity 삼호선 = lineDao.save(new LineEntity("3호선", "blue"));
        LineEntity 사호선 = lineDao.save(new LineEntity("4호선", "purple"));

        sectionDao.save(new SectionEntity(일호선.getId(), 강남역.getId(), 선릉역.getId(), 10));
        sectionDao.save(new SectionEntity(이호선.getId(), 선릉역.getId(), 홍대역.getId(), 10));
        sectionDao.save(new SectionEntity(삼호선.getId(), 잠실역.getId(), 홍대역.getId(), 2));
        sectionDao.save(new SectionEntity(삼호선.getId(), 홍대역.getId(), 신촌역.getId(), 10));
        sectionDao.save(new SectionEntity(삼호선.getId(), 신촌역.getId(), 당산역.getId(), 5));
        sectionDao.save(new SectionEntity(삼호선.getId(), 당산역.getId(), 신림역.getId(), 5));
        sectionDao.save(new SectionEntity(사호선.getId(), 신촌역.getId(), 문래역.getId(), 5));
        sectionDao.save(new SectionEntity(사호선.getId(), 문래역.getId(), 신림역.getId(), 3));

        PathResponse response = pathService.findPath(강남역.getId(), 신림역.getId(), 6);

        //then
        assertAll(() -> {
            assertThat(response.getFare()).isEqualTo(750);
            assertThat(response.getStations().size()).isEqualTo(6);
            assertThat(response.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                    .contains("강남역", "선릉역", "홍대입구역", "신촌역", "문래역", "신림역");
            assertThat(response.getDistance()).isEqualTo(38);
        });
    }

    @DisplayName("청소년 요금제 적용")
    @Test
    void fareForTeen() {
        Station 강남역 = stationDao.save(new Station("강남역"));
        Station 선릉역 = stationDao.save(new Station("선릉역"));
        Station 잠실역 = stationDao.save(new Station("잠실역"));
        Station 홍대역 = stationDao.save(new Station("홍대입구역"));
        Station 신촌역 = stationDao.save(new Station("신촌역"));
        Station 당산역 = stationDao.save(new Station("당산역"));
        Station 문래역 = stationDao.save(new Station("문래역"));
        Station 신림역 = stationDao.save(new Station("신림역"));

        LineEntity 일호선 = lineDao.save(new LineEntity("1호선", "yellow"));
        LineEntity 이호선 = lineDao.save(new LineEntity("2호선", "pink"));
        LineEntity 삼호선 = lineDao.save(new LineEntity("3호선", "blue"));
        LineEntity 사호선 = lineDao.save(new LineEntity("4호선", "purple"));

        sectionDao.save(new SectionEntity(일호선.getId(), 강남역.getId(), 선릉역.getId(), 10));
        sectionDao.save(new SectionEntity(이호선.getId(), 선릉역.getId(), 홍대역.getId(), 10));
        sectionDao.save(new SectionEntity(삼호선.getId(), 잠실역.getId(), 홍대역.getId(), 2));
        sectionDao.save(new SectionEntity(삼호선.getId(), 홍대역.getId(), 신촌역.getId(), 10));
        sectionDao.save(new SectionEntity(삼호선.getId(), 신촌역.getId(), 당산역.getId(), 5));
        sectionDao.save(new SectionEntity(삼호선.getId(), 당산역.getId(), 신림역.getId(), 5));
        sectionDao.save(new SectionEntity(사호선.getId(), 신촌역.getId(), 문래역.getId(), 5));
        sectionDao.save(new SectionEntity(사호선.getId(), 문래역.getId(), 신림역.getId(), 3));

        PathResponse response = pathService.findPath(강남역.getId(), 신림역.getId(), 13);

        //then
        assertAll(() -> {
            assertThat(response.getFare()).isEqualTo(1200);
            assertThat(response.getStations().size()).isEqualTo(6);
            assertThat(response.getStations().stream().map(StationResponse::getName).collect(Collectors.toList()))
                    .contains("강남역", "선릉역", "홍대입구역", "신촌역", "문래역", "신림역");
            assertThat(response.getDistance()).isEqualTo(38);
        });
    }
}
