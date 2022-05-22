package wooteco.subway.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.controller.dto.LineRequest;
import wooteco.subway.controller.dto.LineUpdateRequest;
import wooteco.subway.controller.dto.SectionRequest;
import wooteco.subway.service.dto.LineDto;
import wooteco.subway.service.dto.StationDto;

@SpringBootTest
@Transactional
class LineServiceTest {

    @Autowired
    private LineService lineService;
    @Autowired
    private StationService stationService;

    private StationDto upStation;
    private StationDto downStation;

    @BeforeEach
    void init() {
        upStation = stationService.create("강남역");
        downStation = stationService.create("역삼역");
    }

    @DisplayName("지하철 노선을 저장한다.")
    @Test
    void create() {
        LineDto line = lineService.create(
            new LineRequest("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 1000));
        assertAll(
            () -> assertThat(line.getId()).isGreaterThan(0),
            () -> assertThat(line.getName()).isEqualTo("신분당선"),
            () -> assertThat(line.getColor()).isEqualTo("bg-red-600")
        );
    }

    @DisplayName("이미 존재하는 이름으로 지하철 노선을 생성할 수 없다.")
    @Test
    void duplicateNameException() {
        lineService.create(new LineRequest("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 1000));

        assertThatThrownBy(() -> lineService.create(
            new LineRequest("신분당선", "bg-blue-600", upStation.getId(), downStation.getId(), 10, 1000)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 이름의 지하철 노선이 이미 존재합니다");
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void listLines() {
        lineService.create(new LineRequest("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 1000));
        lineService.create(new LineRequest("2호선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 200));
        lineService.create(new LineRequest("분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 1000));
        List<LineDto> lines = lineService.listLines();

        assertAll(
            () -> assertThat(lines).hasSize(3),
            () -> assertThat(lines).map(LineDto::getName)
                .containsOnly("신분당선", "2호선", "분당선")
        );
    }

    @DisplayName("id로 지하철 노선을 조회한다.")
    @Test
    void findOne() {
        LineDto line = lineService.create(
            new LineRequest("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 1000));
        LineDto foundLine = lineService.findOne(line.getId());

        assertAll(
            () -> assertThat(foundLine.getId()).isEqualTo(line.getId()),
            () -> assertThat(foundLine.getName()).isEqualTo(line.getName()),
            () -> assertThat(foundLine.getColor()).isEqualTo(line.getColor())
        );
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void update() {
        LineDto line = lineService.create(
            new LineRequest("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 1000));
        LineDto updatedLine = lineService.update(
            new LineUpdateRequest("분당선", "bg-blue-600", 500).toEntity(line.getId()));

        assertAll(
            () -> assertThat(updatedLine.getId()).isEqualTo(line.getId()),
            () -> assertThat(updatedLine.getName()).isEqualTo("분당선"),
            () -> assertThat(updatedLine.getColor()).isEqualTo("bg-blue-600"),
            () -> assertThat(updatedLine.getExtraFare()).isEqualTo(500)
        );
    }

    @DisplayName("id로 지하철 노선을 삭제한다.")
    @Test
    void remove() {
        LineDto line = lineService.create(
            new LineRequest("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 1000));
        lineService.remove(line.getId());
        assertThat(lineService.listLines()).isEmpty();
    }

    @DisplayName("하행종점 이후 구간을 추가한다.")
    @Test
    void addSectionLeft() {
        LineDto line = lineService.create(
            new LineRequest("2호선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 200));
        StationDto newStation = stationService.create("교대역");
        SectionRequest newSection = new SectionRequest(downStation.getId(), newStation.getId(), 10);
        lineService.addSection(line.getId(), newSection);

        LineDto updatedLine = lineService.findOne(line.getId());

        assertThat(updatedLine.getStations())
            .map(StationDto::getName)
            .containsExactly("강남역", "역삼역", "교대역");
    }

    @DisplayName("상행종점 이전 구간을 추가한다.")
    @Test
    void addSectionRight() {
        LineDto line = lineService.create(
            new LineRequest("2호선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 200));
        StationDto newStation = stationService.create("교대역");
        SectionRequest newSection = new SectionRequest(
            newStation.getId(), upStation.getId(), 10);
        lineService.addSection(line.getId(), newSection);

        LineDto updatedLine = lineService.findOne(line.getId());

        assertThat(updatedLine.getStations())
            .map(StationDto::getName)
            .containsExactly("교대역", "강남역", "역삼역");
    }

    @DisplayName("상행역이 같은 구간을 추가한다.")
    @Test
    void addSectionLeftToRight() {
        LineDto line = lineService.create(
            new LineRequest("2호선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 200));
        StationDto newStation = stationService.create("교대역");
        SectionRequest newSection = new SectionRequest(
            upStation.getId(), newStation.getId(), 5);

        lineService.addSection(line.getId(), newSection);

        LineDto updatedLine = lineService.findOne(line.getId());
        assertThat(updatedLine.getStations())
            .map(StationDto::getName)
            .containsExactly("강남역", "교대역", "역삼역");
    }

    @DisplayName("하행역이 같은 구간을 추가한다.")
    @Test
    void addSectionRightToLeft() {
        LineDto line = lineService.create(
            new LineRequest("2호선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 200));
        StationDto newStation = stationService.create("교대역");
        SectionRequest newSection = new SectionRequest(newStation.getId(), downStation.getId(), 5);
        lineService.addSection(line.getId(), newSection);

        LineDto updatedLine = lineService.findOne(line.getId());
        assertThat(updatedLine.getStations())
            .map(StationDto::getName)
            .containsExactly("강남역", "교대역", "역삼역");
    }

    @DisplayName("역으로 구간을 삭제한다.")
    @Test
    void deleteSection() {
        LineDto line = lineService.create(
            new LineRequest("2호선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 200));
        StationDto newStation = stationService.create("교대역");
        SectionRequest newSection = new SectionRequest(downStation.getId(), newStation.getId(), 10);
        lineService.addSection(line.getId(), newSection);

        lineService.deleteSection(line.getId(), downStation.getId());

        LineDto updatedLine = lineService.findOne(line.getId());
        assertAll(
            () -> assertThat(updatedLine.getStations())
                .map(StationDto::getName)
                .containsExactly("강남역", "교대역")
        );
    }

    @DisplayName("노선에 구간이 하나밖에 없으면 삭제하지 못한다.")
    @Test
    void deleteSectionEmpty() {
        LineDto line = lineService.create(
            new LineRequest("2호선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 200));

        assertThatThrownBy(() -> lineService.deleteSection(line.getId(), downStation.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("구간이 하나일 땐 삭제할 수 없습니다.");
    }

    @DisplayName("구간을 삭제할 때 노선에 해당하는 역이 없으면 예외가 발생한다.")
    @Test
    void deleteSectionNotExist() {
        LineDto line = lineService.create(
            new LineRequest("2호선", "bg-red-600", upStation.getId(), downStation.getId(), 10, 200));

        assertThatThrownBy(() -> lineService.deleteSection(line.getId(), 3L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("구간이 하나일 땐 삭제할 수 없습니다.");
    }
}
