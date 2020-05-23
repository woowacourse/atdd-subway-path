package wooteco.subway.admin.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationTest {

    @Test
    void isSameIdTest() {
        Station station = new Station(1L, "강남");

        assertThat(station.is(1L)).isTrue();
    }

    @Test
    void isSameNameTest() {
        Station station = new Station(1L, "강남");

        assertThat(station.is("강남")).isTrue();
    }
}