package wooteco.subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.station.Station;

class ConcreteSortStrategyTest {

    private final ConcreteSortStrategy concreteSortStrategy = new ConcreteSortStrategy();

    private final Section _1L_2L_10 = new Section(1L, 1L, 1L, 2L, 10);
    private final Section _2L_3L_10 = new Section(2L, 1L, 2L, 3L, 10);
    private final Section _3L_4L_10 = new Section(2L, 1L, 3L, 4L, 10);
    private final Section _4L_5L_10 = new Section(2L, 1L, 4L, 5L, 10);

    @Test
    @DisplayName("상행역부터 하행역으로 정렬된 역들을 반환한다.")
    void sort() {
        List<Section> sections = new ArrayList<>(List.of(_1L_2L_10, _2L_3L_10, _3L_4L_10, _4L_5L_10));

        Station _1번역 = new Station(1L, "1번역");
        Station _2번역 = new Station(2L, "2번역");
        Station _3번역 = new Station(3L, "3번역");
        Station _4번역 = new Station(4L, "4번역");
        Station _5번역 = new Station(5L, "5번역");

        List<Station> stations = new ArrayList<>(List.of(_1번역, _2번역, _3번역, _4번역, _5번역));
        Collections.shuffle(sections);
        Collections.shuffle(stations);

        List<Station> sortedStations = concreteSortStrategy.sort(sections, stations);
        List<String> sortedStationNames = sortedStations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(sortedStationNames).containsExactly("1번역", "2번역", "3번역", "4번역", "5번역");
    }
}
