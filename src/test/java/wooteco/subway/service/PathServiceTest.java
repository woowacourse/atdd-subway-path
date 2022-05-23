package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.CENTER;
import static wooteco.subway.Fixtures.DOWN;
import static wooteco.subway.Fixtures.GREEN;
import static wooteco.subway.Fixtures.HYEHWA;
import static wooteco.subway.Fixtures.LEFT;
import static wooteco.subway.Fixtures.LINE_2;
import static wooteco.subway.Fixtures.LINE_4;
import static wooteco.subway.Fixtures.RIGHT;
import static wooteco.subway.Fixtures.SKY_BLUE;
import static wooteco.subway.Fixtures.UP;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private PathService pathService;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private LineRepository lineRepository;

    private Station up;
    private Station left;
    private Station center;
    private Station right;
    private Station down;

    @BeforeEach
    void setUp() {
        final Long upId = stationRepository.save(UP);
        final Long leftId = stationRepository.save(LEFT);
        final Long centerId = stationRepository.save(CENTER);
        final Long rightId = stationRepository.save(RIGHT);
        final Long downId = stationRepository.save(DOWN);

        up = stationRepository.findById(upId);
        left = stationRepository.findById(leftId);
        center = stationRepository.findById(centerId);
        right = stationRepository.findById(rightId);
        down = stationRepository.findById(downId);

        final Long line4Id = lineRepository.save(new Line(LINE_4, SKY_BLUE, 0));
        final Long line2Id = lineRepository.save(new Line(LINE_2, GREEN, 900));

        sectionRepository.save(line4Id, new Section(up, center, 5));
        sectionRepository.save(line4Id, new Section(center, down, 6));
        sectionRepository.save(line2Id, new Section(left, center, 20));
        sectionRepository.save(line2Id, new Section(center, right, 50));
    }

    @Test
    @DisplayName("경로를 조회한다. 최단거리와 요금을 계산한다.")
    void findPath() {
        final PathResponse pathResponse = pathService.find(up.getId(), down.getId(), 20);

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(new StationResponse(up),
                        new StationResponse(center), new StationResponse(down)),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(11),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1_350)

        );
    }

    @Test
    @DisplayName("경로를 조회할 수 없는 경우, 예외를 발생시킨다.")
    void findPath_noPath() {
        assertThatThrownBy(() -> pathService.find(up.getId(), up.getId(), 20))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("환승하는 경로를 조회한다. 최단거리와 요금을 계산한다.")
    void findPath_crossLine() {
        final PathResponse pathResponse = pathService.find(up.getId(), left.getId(), 20);

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(new StationResponse(up),
                        new StationResponse(center), new StationResponse(left)),

                () -> assertThat(pathResponse.getDistance()).isEqualTo(25),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2_450)
        );
    }

    @Test
    @DisplayName("최단 거리에 대한 요금 계산 시, 청소년 할인을 적용한다.")
    void calculateFare_teenager() {
        final PathResponse pathResponse = pathService.find(up.getId(), down.getId(), 15);

        assertThat(pathResponse.getFare()).isEqualTo(1_150);
    }

    @Test
    @DisplayName("최단 거리에 대한 요금 계산 시, 어린이 할인을 적용한다.")
    void calculateFare_child() {
        final PathResponse pathResponse = pathService.find(up.getId(), down.getId(), 10);

        assertThat(pathResponse.getFare()).isEqualTo(850);
    }

    @Test
    @DisplayName("최단 거리에 대한 요금 계산 시, 아기 할인을 적용한다.")
    void calculateFare_baby() {
        final PathResponse pathResponse = pathService.find(up.getId(), down.getId(), 5);

        assertThat(pathResponse.getFare()).isEqualTo(0);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선 이용 시, 추가 요금을 부과한다.")
    void calculateFare_extraFare() {
        final PathResponse pathResponse = pathService.find(left.getId(), right.getId(), 26);

        assertThat(pathResponse.getFare()).isEqualTo(3_250);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선을 여러개 이용 시, 최대 추가 요금만을 부과한다.")
    void calculateFare_maxExtraFare() {
        // given
        final Long stationId = stationRepository.save(new Station(HYEHWA));
        final Station station = stationRepository.findById(stationId);
        final Long lineId = lineRepository.save(new Line("엘리역", "bg-yellow-600", 1_000));
        sectionRepository.save(lineId, new Section(station, center, 10));

        // when
        final PathResponse pathResponse = pathService.find(station.getId(), left.getId(), 25);

        // then
        assertThat(pathResponse.getFare()).isEqualTo(2_650);
    }
}
