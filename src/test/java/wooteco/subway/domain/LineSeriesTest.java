package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.fixture.LineFixture;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.exception.RowDuplicatedException;
import wooteco.subway.exception.RowNotFoundException;

class LineSeriesTest {

    @Test
    @DisplayName("중복된 이름인 경우 예외를 던진다.")
    public void throwsExceptionWithDuplicatedName() {
        // given
        LineSeries series = new LineSeries(List.of(LineFixture.getLineAb()));
        // when
        Line line = new Line("분당선", "color3", 500);
        // then
        assertThatExceptionOfType(RowDuplicatedException.class)
            .isThrownBy(() -> series.add(line));
    }

    @Test
    @DisplayName("노선을 추가한다")
    public void addLine() {
        // given
        LineSeries series = new LineSeries(List.of(LineFixture.getLineAb()));

        // when
        series.add(new Line("새로운 역", "새로운 색", 500));

        // then
        assertThat(series.getLines()).hasSize(2);
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    public void deleteLine() {
        // given
        LineSeries series = new LineSeries(List.of(LineFixture.getLineAb()));

        // when
        series.delete(1L);

        // then
        assertThat(series.getLines()).hasSize(0);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 노선을 삭제하는 경우 예외를 던진다.")
    public void throwsExceptionWithEmptyIdOnDelete() {
        // given & when
        LineSeries series = new LineSeries(List.of(LineFixture.getLineAb()));
        // then
        assertThatExceptionOfType(RowNotFoundException.class)
            .isThrownBy(() -> series.delete(3L));
    }

    @Test
    @DisplayName("노선을 업데이트한다")
    public void updateLine() {
        // given
        LineSeries series = new LineSeries(List.of(LineFixture.getLineAb()));

        // when
        series.update(new Line(LineFixture.getLineAb().getId(), "뉴네임", "뉴컬러", 500));

        // then
        assertThat(series.getLines().get(0).getName()).isEqualTo("뉴네임");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 노선을 수정하는 경우 예외를 던진다.")
    public void throwsExceptionWithEmptyIdOnUpdate() {
        // given & when
        LineSeries series = new LineSeries(List.of(LineFixture.getLineAb()));
        // then
        assertThatExceptionOfType(RowNotFoundException.class)
            .isThrownBy(() -> series.update(new Line(999L, "뉴네임", "뉴컬러", 500)));
    }
}