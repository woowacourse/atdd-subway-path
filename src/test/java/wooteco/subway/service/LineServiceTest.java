package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.jdbc.JdbcLineDao;
import wooteco.subway.dao.jdbc.JdbcSectionDao;
import wooteco.subway.dao.jdbc.JdbcStationDao;
import wooteco.subway.service.dto.line.LineRequestDto;
import wooteco.subway.service.dto.line.LineResponse;
import wooteco.subway.service.dto.section.SectionRequestDto;
import wooteco.subway.service.dto.station.StationResponse;

@JdbcTest
@Sql(scripts = {"classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LineServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineService lineService;
    private StationService stationService;
    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        lineService = new LineService(new JdbcLineDao(jdbcTemplate),
                new StationService(new JdbcStationDao(jdbcTemplate)),
                new SectionService(new JdbcSectionDao(jdbcTemplate)));
        stationService = new StationService(new JdbcStationDao(jdbcTemplate));
        sectionService = new SectionService(new JdbcSectionDao(jdbcTemplate));

        stationService.createStation("낙성대");
        stationService.createStation("교대");
        stationService.createStation("선릉");
    }

    @Test
    @DisplayName("노선 생성")
    void saveLine() {
        //given
        LineRequestDto lineRequestDto = new LineRequestDto("2호선", "bg-green-300", 1L, 2L, 10);
        lineService.create(lineRequestDto);
        sectionService.create(new SectionRequestDto(1L, 2L, 3L, 20));
        List<StationResponse> expected = new ArrayList<>();
        expected.add(new StationResponse(1L, "낙성대"));
        expected.add(new StationResponse(2L, "교대"));
        expected.add(new StationResponse(3L, "선릉"));
        //when
        LineResponse findLineResponse = lineService.findById(1L);
        List<StationResponse> actual = findLineResponse.getStations();
        //then
        assertAll(
                () -> assertThat(findLineResponse.getId()).isEqualTo(1L),
                () -> assertThat(findLineResponse.getName()).isEqualTo("2호선"),
                () -> assertThat(findLineResponse.getColor()).isEqualTo("bg-green-300"),
                () -> assertThat(expected).isEqualTo(actual)
        );
    }

    @Test
    @DisplayName("중복 노선 생성시 예외 발생")
    void duplicateLineName() {
        //given
        //when
        lineService.create(new LineRequestDto("2호선", "bg-green-300", 1L, 2L, 10));
        //then
        assertThatThrownBy(() -> lineService.create(new LineRequestDto("2호선", "bg-green-300", 2L, 3L, 10)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선 조회")
    void findLine() {
        //given
        LineRequestDto lineRequestDto = new LineRequestDto("2호선", "bg-green-300", 1L, 2L, 10);
        //when
        LineResponse lineResponse = lineService.create(lineRequestDto);
        LineResponse findResult = lineService.findById(lineResponse.getId());

        assertAll(
                () -> assertThat(findResult.getId()).isEqualTo(lineResponse.getId()),
                () -> assertThat(findResult.getName()).isEqualTo("2호선"),
                () -> assertThat(findResult.getColor()).isEqualTo("bg-green-300")
        );
    }

    @Test
    @DisplayName("노선 조회 실패")
    void findLineFail() {
        //given
        //when
        //then
        assertThatThrownBy(() -> lineService.findById(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("노선 목록 조회")
    void findAllLine() {
        //given
        LineRequestDto lineRequest1 = new LineRequestDto("1호선", "bg-blue-200", 1L, 2L, 10);
        LineRequestDto lineRequest2 = new LineRequestDto("2호선", "bg-green-300", 2L, 3L, 20);
        LineResponse lineResponse1 = lineService.create(lineRequest1);
        LineResponse lineResponse2 = lineService.create(lineRequest2);
        //when
        List<Long> ids = lineService.findAll().stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        //then
        assertAll(
                () -> assertThat(ids.contains(lineResponse1.getId())).isTrue(),
                () -> assertThat(ids.contains(lineResponse2.getId())).isTrue()
        );
    }

    @Test
    @DisplayName("노선 업데이트 성공")
    void updateLine() {
        //given
        LineRequestDto lineRequestDto = new LineRequestDto("1호선", "bg-blue-200", 1L, 2L, 10);
        LineResponse lineResponse = lineService.create(lineRequestDto);
        //when
        LineRequestDto newLineRequestDto = new LineRequestDto("2호선", "bg-green-300", 1L, 2L, 10);
        lineService.updateById(lineResponse.getId(), newLineRequestDto);
        LineResponse response = lineService.findById(lineResponse.getId());
        //then
        assertThat(response.getName()).isEqualTo("2호선");
        assertThat(response.getColor()).isEqualTo("bg-green-300");
    }

    @Test
    @DisplayName("노선 업데이트 실패")
    void failUpdateLine() {
        //given
        LineRequestDto lineRequestDto1 = new LineRequestDto("1호선", "bg-blue-200", 1L, 2L, 10);
        lineService.create(lineRequestDto1);
        //when
        LineRequestDto lineRequestDto2 = new LineRequestDto("2호선", "bg-green-300", 2L, 3L, 20);
        LineResponse lineResponse = lineService.create(lineRequestDto2);
        //then
        assertAll(
                () -> assertThatThrownBy(
                        () -> lineService.updateById(-1L, new LineRequestDto("2호선", "bg-black-500", 1L, 2L, 10)))
                        .isInstanceOf(NoSuchElementException.class),
                () -> assertThatThrownBy(() -> lineService.updateById(lineResponse.getId(),
                        new LineRequestDto("1호선", "bg-black-500", 1L, 2L, 10)))
                        .isInstanceOf(NoSuchElementException.class),
                () -> assertThatThrownBy(() -> lineService.updateById(lineResponse.getId(),
                        new LineRequestDto("3호선", "bg-blue-200", 1L, 2L, 10)))
                        .isInstanceOf(NoSuchElementException.class)
        );
    }

    @Test
    @DisplayName("노선 삭제")
    void deleteLine() {
        //given
        LineRequestDto lineRequestDto = new LineRequestDto("1호선", "bg-blue-200", 1L, 2L, 10);
        Long id = lineService.create(lineRequestDto).getId();
        //when
        lineService.deleteById(id);
        //then
        List<Long> ids = lineService.findAll().stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(ids).doesNotContain(id);
    }

    @Test
    @DisplayName("없는 노선 삭제요청 시 예외 발생")
    void invalidLine() {
        //given
        //when
        //then
        assertThatThrownBy(() -> lineService.deleteById(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
