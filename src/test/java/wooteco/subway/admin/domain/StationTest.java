package wooteco.subway.admin.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationTest {

    @Test
    void isIdTest() {
        Station station = new Station(1L, "강남");

        assertThat(station.isSameId(1L)).isTrue();
    }

    @Test
    void isNameTest() {
        Station station = new Station(1L, "강남");

        assertThat(station.isSameId("강남")).isTrue();
    }
}