package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;
import wooteco.subway.exception.duplicate.DuplicateStationException;
import wooteco.subway.exception.invalidrequest.InvalidPathRequestException;

class DijkstraPathStrategyTest {

    private final Station gangnam = new Station("강남역");
    private final Station yeoksam = new Station("역삼역");
    private final Station seolleung = new Station("선릉역");
    private final Line line = new Line("2호선", "초록색", 0);

    @DisplayName("각 구간 리스트들을 통해서 최단 경로를 조회한다.")
    @Test
    void getDijkstraShortestPath() {
        PathStrategy pathStrategy = new DijkstraPathStrategy();

        Section gangnam_yeoksam = new Section(line, gangnam, yeoksam, 1);
        Section yeoksam_seolleung = new Section(line, yeoksam, seolleung, 1);
        Sections sections = new Sections(List.of(gangnam_yeoksam, yeoksam_seolleung));

        Path shortestPath = pathStrategy.calculatePath(gangnam, seolleung, sections);
        Path expected = new Path(List.of(gangnam, yeoksam, seolleung), List.of(line), 2);

        assertThat(shortestPath).isEqualTo(expected);
    }

    @DisplayName("경로의 시작역과 종점역이 같을 경우 예외가 발생한다.")
    @Test
    void sameSourceTarget() {
        PathStrategy pathStrategy = new DijkstraPathStrategy();

        Section gangnam_yeoksam = new Section(line, gangnam, yeoksam, 1);
        Section yeoksam_seolleung = new Section(line, yeoksam, seolleung, 1);
        Sections sections = new Sections(List.of(gangnam_yeoksam, yeoksam_seolleung));

        assertThatThrownBy(() -> pathStrategy.calculatePath(gangnam, gangnam, sections))
                .isInstanceOf(DuplicateStationException.class)
                .hasMessage("경로의 시작과 끝은 같은 역일 수 없습니다.");
    }

    @DisplayName("경로가 끊겨 있어 목적지까지 도착할 수 없을 경우 예외가 발생한다.")
    @Test
    void disconnectedPath() {
        PathStrategy pathStrategy = new DijkstraPathStrategy();

        Section gangnam_yeoksam = new Section(line, gangnam, yeoksam, 1);
        Sections sections = new Sections(List.of(gangnam_yeoksam));

        assertThatThrownBy(() -> pathStrategy.calculatePath(gangnam, seolleung, sections))
                .isInstanceOf(InvalidPathRequestException.class)
                .hasMessage("목적지까지 도달할 수 없습니다.");
    }

}
