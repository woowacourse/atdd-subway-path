package wooteco.subway.admin.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationTest {

    @Test
    void is() {
        Station station = new Station(1L, "강남");

        assertThat(station.is(1L)).isTrue();
    }
}