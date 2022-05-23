package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.BLUE;
import static wooteco.subway.Fixtures.CENTER;
import static wooteco.subway.Fixtures.DOWN;
import static wooteco.subway.Fixtures.LEFT;
import static wooteco.subway.Fixtures.LINE_1;
import static wooteco.subway.Fixtures.LINE_2;
import static wooteco.subway.Fixtures.RED;
import static wooteco.subway.Fixtures.RIGHT;
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

        up = stationRepository.getById(upId);
        left = stationRepository.getById(leftId);
        center = stationRepository.getById(centerId);
        right = stationRepository.getById(rightId);
        down = stationRepository.getById(downId);

        final Long line2Id = lineRepository.save(new Line(LINE_1, RED));
        final Long line4Id = lineRepository.save(new Line(LINE_2, BLUE));

        sectionRepository.save(line2Id, new Section(up, center, 5));
        sectionRepository.save(line2Id, new Section(center, down, 6));
        sectionRepository.save(line4Id, new Section(left, center, 20));
        sectionRepository.save(line4Id, new Section(center, right, 50));
    }

    @Test
    @DisplayName("경로를 조회한다. 최단거리와 요금을 계산한다.")
    void findPath() {
        final PathResponse pathResponse = pathService.findPath(up.getId(), down.getId());

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(new StationResponse(up),
                        new StationResponse(center), new StationResponse(down)),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(11),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1350)

        );
    }

    @Test
    @DisplayName("환승하는 경로를 조회한다. 최단거리와 요금을 계산한다.")
    void findPath_crossLine() {
        final PathResponse pathResponse = pathService.findPath(up.getId(), left.getId());

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(new StationResponse(up),
                        new StationResponse(center), new StationResponse(left)),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(25),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1550)
        );
    }
}
