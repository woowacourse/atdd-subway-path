package wooteco.subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dao.entity.SectionEntity;

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

        SectionEntity 강남_선릉 = sectionDao.save(new SectionEntity(일호선.getId(), 강남역.getId(), 선릉역.getId(), 3));
        SectionEntity 강남_잠실 = sectionDao.save(new SectionEntity(삼호선.getId(), 강남역.getId(), 잠실역.getId(), 3));
        SectionEntity 강남_홍대 = sectionDao.save(new SectionEntity(이호선.getId(), 강남역.getId(), 홍대역.getId(), 10));
        SectionEntity 선릉_홍대 = sectionDao.save(new SectionEntity(일호선.getId(), 선릉역.getId(), 홍대역.getId(), 2));
        SectionEntity 잠실_홍대 = sectionDao.save(new SectionEntity(삼호선.getId(), 잠실역.getId(), 홍대역.getId(), 5));

        //when
        PathResponse response = pathService.findPath(강남역.getId(), 홍대역.getId(), 10);

        //then
        assertAll(() -> {
            assertThat(response.getFare()).isEqualTo(1250);
            assertThat(response.getDistance()).isEqualTo(5);
            assertThat(response.getStations().get(0).getName()).isEqualTo("강남역");
            assertThat(response.getStations().get(1).getName()).isEqualTo("선릉역");
            assertThat(response.getStations().get(2).getName()).isEqualTo("홍대입구역");
        });
    }
}
