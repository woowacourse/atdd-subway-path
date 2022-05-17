package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.jdbc.JdbcLineDao;
import wooteco.subway.dao.jdbc.JdbcSectionDao;
import wooteco.subway.dao.jdbc.JdbcStationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.service.dto.PathRequestDto;
import wooteco.subway.service.dto.PathResponseDto;
import wooteco.subway.service.dto.line.LineRequestDto;
import wooteco.subway.service.dto.section.SectionRequestDto;
import wooteco.subway.service.dto.station.StationResponseDto;

@JdbcTest
class PathServiceTest {

    private LineService lineService;
    private StationService stationService;
    private SectionService sectionService;
    private PathService pathService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        lineService = new LineService(new JdbcLineDao(jdbcTemplate), new StationService(new JdbcStationDao(jdbcTemplate)), new SectionService(new JdbcSectionDao(jdbcTemplate)));
        stationService = new StationService(new JdbcStationDao(jdbcTemplate));
        sectionService = new SectionService(new JdbcSectionDao(jdbcTemplate));
        pathService = new PathService(stationService, sectionService);
        stationService.createStation("에덴");
        stationService.createStation("제로");
        stationService.createStation("서초");
        stationService.createStation("교대");
        stationService.createStation("선릉");
        lineService.create(new LineRequestDto("2호선", "bg-green-300", 1L, 2L, 50));
        sectionService.create(new SectionRequestDto(1L, 1L, 3L, 10));
        sectionService.create(new SectionRequestDto(1L, 3L, 5L, 20));
        sectionService.create(new SectionRequestDto(1L, 4L, 5L, 10));
    }

    @Test
    @DisplayName("경로를 조회한다.")
    void findPath() {
        //given
        Long source = 1L;
        Long target = 5L;
        int age = 15;
        //when
        PathResponseDto pathResponseDto = pathService.getPath(new PathRequestDto(source, target, age));
        //then
        assertAll(
                () -> assertThat(pathResponseDto.getStations().size()).isEqualTo(4),
                () -> assertThat(pathResponseDto.getDistance()).isEqualTo(30),
                () -> assertThat(pathResponseDto.getFare()).isEqualTo(1650)
        );
    }
//
//    @Test
//    @DisplayName("중복된 경로가 있다면 가중치가 낮은 거리가 선택된다")
//    void FindPathWithDuplicatedNodes() {
//        //given
//        List<Long> stationIds = stationService.findStations().stream()
//                .map(StationResponseDto::getId)
//                .collect(Collectors.toList());
//        lineService.create(new LineRequestDto("3호선", "bg-black-100", 1L, 3L, 5));
//        Sections sections = sectionService.findAll();
//        Path path = new Path(1L, 5L, stationIds, sections);
//        //when
//        List<Long> actualPath = path.getShortestPath();
//        int actualTotalDistance = path.getTotalDistance();
//        //then
//        assertAll(
//                () -> assertThat(actualPath).containsExactly(1L, 3L, 4L, 5L),
//                () -> assertThat(actualTotalDistance).isEqualTo(25)
//        );
//    }
//
//    @Test
//    @DisplayName("없는 출발 역 또는 도착 역 Id 를 입력받으면 예외를 반환한다.")
//    void FindPathWithNotExistsStationId() {
//        //given
//        List<Long> stationIds = stationService.findStations().stream()
//                .map(StationResponseDto::getId)
//                .collect(Collectors.toList());
//        Sections sections = sectionService.findAll();
//        //when
//        //then
//        assertThatThrownBy(() -> new Path(1L, -1L, stationIds, sections))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("[ERROR] 역을 찾을 수 없습니다");
//    }
}
