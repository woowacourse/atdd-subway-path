package wooteco.subway.domain.property.fare;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.domain.path.JgraphtPathFinder;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;
import wooteco.subway.domain.property.Age;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

class FarePoliciesTest {

    @Test
    @DisplayName("운임을 순서에 따라 일괄 적용한다.(거리, 나이)")
    public void applyPoliciesInOrderForDistanceAndAge() {
        // given
        FarePolicies policies = getFarePolicies(0, 15, 10);
        // when
        final int calculated = policies.applyAll(1250);
        // then
        assertThat(calculated).isEqualTo(500);
    }

    @Test
    @DisplayName("운임을 순서에 따라 일괄 적용한다.(거리, 노선)")
    public void applyPoliciesInOrderForDistanceAndExtraFare() {
        // given
        FarePolicies policies = getFarePolicies(500, 20, 25);
        // when
        final int calculated = policies.applyAll(1250);
        // then
        assertThat(calculated).isEqualTo(1950);
    }

    @Test
    @DisplayName("운임을 순서에 따라 일괄 적용한다.(나이, 노선)")
    public void applyPoliciesInOrderForAgeAndExtraFare() {
        // given
        FarePolicies policies = getFarePolicies(500, 10, 15);
        // when
        final int calculated = policies.applyAll(1250);

        // then
        assertThat(calculated).isEqualTo(1120);
    }

    @Test
    @DisplayName("운임을 순서에 따라 일괄 적용한다.(전체)")
    public void applyPoliciesInOrderForAll() {
        // given
        FarePolicies policies = getFarePolicies(500, 55, 15);
        // when
        final int calculated = policies.applyAll(1250);

        // then
        assertThat(calculated).isEqualTo(1840);
    }

    private FarePolicies getFarePolicies(int extraFare, int distance, int age) {
        final Station source = new Station(1L, "출발역");
        final Station destination = new Station(2L, "도착역");
        final PathFinder finder = JgraphtPathFinder.of(
            new LineSeries(List.of(new Line(1L, "신분당선", "color", extraFare,
                List.of(new Section(1L, source, destination, new Distance(distance)))))),
            source,
            destination
        );
        return FarePolicies.of(finder, new Age(age));
    }
}