package wooteco.subway.admin.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.admin.domain.graph.Graph;
import wooteco.subway.admin.domain.graph.SubwayGraphKey;
import wooteco.subway.admin.domain.graph.SubwayGraphStrategy;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayGraphKeyTest {

    @DisplayName("SubwayGraphKey의 String을 입력하면 해당 SubwayGraphKey 반환")
    @ParameterizedTest
    @CsvSource(value = {"distance,DISTANCE", "duration,DURATION"})
    void of(String symbol, SubwayGraphKey key) {
        assertThat(SubwayGraphKey.of(symbol)).isEqualTo(key);
    }

    @DisplayName("SubwayGraphKey에 없는 String을 입력하면 예외 발생")
    @Test
    void ofException() {
        assertThatThrownBy(() -> SubwayGraphKey.of("bibab"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("SubwayGraphKey의 개수만큼 graph 생성하기")
    @Test
    void makeGraph() {
        //given
        Map<SubwayGraphKey, Graph> subwayGraphMap = SubwayGraphKey.makeGraph(new HashSet<>(), new SubwayGraphStrategy());

        //when
        Set<SubwayGraphKey> subwayGraphKeys = subwayGraphMap.keySet();

        //then
        assertThat(subwayGraphKeys).contains(SubwayGraphKey.DISTANCE, SubwayGraphKey.DURATION);
    }
}