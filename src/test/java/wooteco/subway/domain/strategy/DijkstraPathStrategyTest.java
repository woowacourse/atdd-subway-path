package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.duplicate.DuplicateStationException;

class DijkstraPathStrategyTest {

    private final Station gangnam = new Station("강남역");
    private final Station yeoksam = new Station("역삼역");
    private final Station seolleung = new Station("선릉역");

    @DisplayName("각 구간 리스트들을 통해서 최단 경로를 조회한다.")
    @Test
    void getDijkstraShortestPath() {
        PathStrategy pathStrategy = new DijkstraPathStrategy();

        Section gangnam_yeoksam = new Section(gangnam, yeoksam, 1);
        Section yeoksam_seolleung = new Section(yeoksam, seolleung, 1);
        Sections sections = new Sections(List.of(gangnam_yeoksam, yeoksam_seolleung));

        Path shortestPath = pathStrategy.calculatePath(gangnam, seolleung, sections);
        Path expected = new Path(List.of(gangnam, yeoksam, seolleung), 2);

        assertThat(shortestPath).isEqualTo(expected);
    }

    @Test
    @DisplayName("경로의 시작역과 종점역이 같을 경우 예외가 발생한다.")
    void sameSourceTarget() {
        PathStrategy pathStrategy = new DijkstraPathStrategy();

        Section gangnam_yeoksam = new Section(gangnam, yeoksam, 1);
        Section yeoksam_seolleung = new Section(yeoksam, seolleung, 1);
        Sections sections = new Sections(List.of(gangnam_yeoksam, yeoksam_seolleung));

        assertThatThrownBy(() -> pathStrategy.calculatePath(gangnam, gangnam, sections))
                .isInstanceOf(DuplicateStationException.class)
                .hasMessage("경로의 시작과 끝은 같은 역일 수 없습니다.");
    }

}
