package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.domain.fixture.SectionFixture.*;
import static wooteco.subway.domain.fixture.StationFixture.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionSeries;
import wooteco.subway.exception.RowNotFoundException;
import wooteco.subway.exception.SectionNotEnoughException;

class SectionSeriesTest {

    @Test
    @DisplayName("상행에서 하행을 잇는다.")
    public void findUpToDownSection() {
        // given
        SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));
        // when
        sectionSeries.add(new Section(3L, getStationB(), getStationX(), new Distance(3)));
        // then

        assertThat(sectionSeries.getSections()).hasSize(3);
    }

    @Test
    @DisplayName("하행에서 상행을 잇는다.")
    public void findDownToUpSection() {
        // given
        SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));
        // when
        sectionSeries.add(new Section(3L, getStationX(), getStationB(), new Distance(3)));
        // then
        assertThat(sectionSeries.getSections()).hasSize(3);
    }

    @Test
    @DisplayName("상행에 종점을 잇는다.")
    public void findUpToEndSection() {
        // given
        SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));

        // when
        sectionSeries.add(new Section(3L, getStationX(), getStationA(), new Distance(3)));

        // then
        assertThat(sectionSeries.getSections()).hasSize(3);
    }

    @Test
    @DisplayName("하행에 종점을 잇는다.")
    public void findDownToEndSection() {
        // given
        SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));

        // when
        sectionSeries.add(new Section(3L, getStationC(), getStationX(), new Distance(3)));

        // then
        assertThat(sectionSeries.getSections()).hasSize(3);
    }

    @Test
    @DisplayName("중복 노선을 등록하면 예외를 던진다.")
    public void throwsExceptionWithDuplicatedSection() {
        // given
        final SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));
        // when
        final Section newSection = new Section(3L, getStationA(), getStationB(), new Distance(3));
        // then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sectionSeries.add(newSection));
    }

    @Test
    @DisplayName("기존보다 더 넓은 범위의 중복 노선을 등록하면 예외를 던진다.")
    public void throwsExceptionWithBroadDuplicatedSection() {
        // given
        final SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));
        // when
        final Section newSection = new Section(3L, getStationA(), getStationC(), new Distance(3));
        // then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sectionSeries.add(newSection));
    }

    @Test
    @DisplayName("관련없는 노선을 등록하려는 경우 예외를 던진다.")
    public void throwsExceptionWithUnrelatedSection() {
        // given
        final SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));
        // when
        final Section newSection = new Section(3L, getStationX(), getStationY(), new Distance(3));
        // then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sectionSeries.add(newSection));
    }

    @Test
    @DisplayName("등록하려는 구간 거리가 기존보다 짧거나 같으면 예외를 던진다.")
    public void throwsExceptionWithShorterDistance() {
        // given
        final SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));
        // when
        final Section newSection = new Section(3L, getStationA(), getStationX(), new Distance(100));
        // then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sectionSeries.add(newSection));
    }

    @Test
    @DisplayName("구간이 부족하면 삭제할수 없다.")
    public void throwsExceptionWhenSectionsNotEnough() {
        // given & when
        final SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb()));
        // then
        assertThatExceptionOfType(SectionNotEnoughException.class)
                .isThrownBy(() -> sectionSeries.remove(getStationA()));
    }

    @Test
    @DisplayName("중간 구간을 삭제한다.")
    public void removeSection() {
        // given
        final SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));

        // when
        sectionSeries.remove(getStationB());

        // then
        assertThat(sectionSeries.getSections()).hasSize(1);
    }

    @Test
    @DisplayName("상행 종점을 삭제한다.")
    public void removeUpTerminal() {
        // given
        final SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));

        // when
        sectionSeries.remove(getStationA());

        // then
        assertThat(sectionSeries.getSections()).hasSize(1);
    }

    @Test
    @DisplayName("하행 종점을 삭제한다.")
    public void removeDownTerminal() {
        // given
        final SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));

        // when
        sectionSeries.remove(getStationC());

        // then
        assertThat(sectionSeries.getSections()).hasSize(1);
    }

    @Test
    @DisplayName("삭제하려는 역이 구간에 등록되어 있지 않으면 예외를 던진다.")
    public void throwsExceptionWithEmptyId() {
        // given & when
        final SectionSeries sectionSeries = new SectionSeries(List.of(getSectionAb(), getSectionBc()));
        // then
        assertThatExceptionOfType(RowNotFoundException.class)
                .isThrownBy(() -> sectionSeries.remove(getStationX()));
    }
}
