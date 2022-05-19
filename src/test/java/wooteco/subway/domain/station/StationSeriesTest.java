package wooteco.subway.domain.station;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.fixture.SectionFixture;
import wooteco.subway.domain.section.Section;
import wooteco.subway.exception.IdMissingException;
import wooteco.subway.exception.RowDuplicatedException;
import wooteco.subway.exception.RowNotFoundException;

class StationSeriesTest {

    @Test
    @DisplayName("ID가 없으면 예외를 던진다.")
    public void throwsExceptionWithNullIdStation() {
        // given
        Station stationA = new Station("A");
        Station stationB = new Station(2L, "B");
        // when
        final List<Station> stations = List.of(stationA, stationB);
        // then
        assertThatExceptionOfType(IdMissingException.class)
            .isThrownBy(() -> new StationSeries(stations));
    }

    @Test
    @DisplayName("역을 추가한다.")
    public void addStation() {
        // given
        StationSeries series = new StationSeries(List.of(new Station(1L, "A"), new Station(2L, "B")));
        // when
        series.add(new Station(3L, "C"));
        // then
        assertThat(series.getStations()).hasSize(3);
    }

    @Test
    @DisplayName("역을 추가할 때 이름이 중복되면 예외를 던진다.")
    public void throwsExceptionWithDuplicatedName() {
        // given
        StationSeries series = new StationSeries(List.of(new Station(1L, "A"), new Station(2L, "B")));

        // when
        final Station addStation = new Station(3L, "B");

        assertThatExceptionOfType(RowDuplicatedException.class)
            .isThrownBy(() -> series.add(addStation));
        // then
    }

    @Test
    @DisplayName("역을 삭제한다")
    public void removeStation() {
        // given
        StationSeries series = new StationSeries(List.of(new Station(1L, "A"), new Station(2L, "B")));

        // when
        series.delete(1L);

        // then
        assertThat(series.getStations()).hasSize(1);
    }

    @Test
    @DisplayName("해당하는 ID로 역을 삭제하는 경우 예외를 던진다.")
    public void throwsExceptionWithEmptyId() {
        // given
        StationSeries series = new StationSeries(List.of(new Station(1L, "A"), new Station(2L, "B")));

        // when
        final long deleteId = 3L;

        // then
        assertThatExceptionOfType(RowNotFoundException.class)
            .isThrownBy(() -> series.delete(deleteId));
    }

    @Test
    @DisplayName("구간으로부터 정렬된 역 시리즈를 얻는다.")
    public void getStationSeriesAsOrdered() {
        // given
        List<Section> sections = List.of(SectionFixture.getSectionAb(), SectionFixture.getSectionBc());
        // when
        final StationSeries stationSeries = StationSeries.fromSectionsAsOrdered(sections);
        // then
        assertThat(stationSeries.getStations()).hasSize(3);
    }
}