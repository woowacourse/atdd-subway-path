package wooteco.subway.domain.property.fare;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.property.Age;
import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.station.Station;

class FarePoliciesTest {

    @Test
    @DisplayName("운임을 순서에 따라 일괄 적용한다.(거리, 나이)")
    public void applyPoliciesInOrderForDistanceAndAge() {
        // given
        Path path = new Path(
            List.of(new Station("A"), new Station("B")),
            List.of(new Fare(0), new Fare(0)),
            new Distance(15)
        );
        FarePolicies policies = FarePolicies.of(path, new Age(10));
        // when
        final Fare calculated = policies.calculate(new Fare());
        // then
        assertThat(calculated.getAmount()).isEqualTo(500);
    }

    @Test
    @DisplayName("운임을 순서에 따라 일괄 적용한다.(거리, 노선)")
    public void applyPoliciesInOrderForDistanceAndExtraFare() {
        // given
        Path path = new Path(
            List.of(new Station("A"), new Station("B")),
            List.of(new Fare(500), new Fare(300)),
            new Distance(20)
        );
        FarePolicies policies = FarePolicies.of(path, new Age(25));
        // when
        final Fare calculated = policies.calculate(new Fare());
        // then
        assertThat(calculated.getAmount()).isEqualTo(1950);
    }

    @Test
    @DisplayName("운임을 순서에 따라 일괄 적용한다.(나이, 노선)")
    public void applyPoliciesInOrderForAgeAndExtraFare() {
        // given
        Path path = new Path(
            List.of(new Station("A"), new Station("B")),
            List.of(new Fare(500), new Fare(300)),
            new Distance(10)
        );
        FarePolicies policies = FarePolicies.of(path, new Age(15));
        // when
        final Fare calculated = policies.calculate(new Fare());
        // then
        assertThat(calculated.getAmount()).isEqualTo(1120);
    }

    @Test
    @DisplayName("운임을 순서에 따라 일괄 적용한다.(전체)")
    public void applyPoliciesInOrderForAll() {
        // given
        Path path = new Path(
            List.of(new Station("A"), new Station("B")),
            List.of(new Fare(500), new Fare(300)),
            new Distance(55)
        );
        FarePolicies policies = FarePolicies.of(path, new Age(15));
        // when
        final Fare calculated = policies.calculate(new Fare());
        // then
        assertThat(calculated.getAmount()).isEqualTo(1840);
    }
}