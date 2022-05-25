package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.fixture.LineFixture;
import wooteco.subway.exception.RowDuplicatedException;
import wooteco.subway.exception.RowNotFoundException;

class LineSeriesTest {

    @Test
    @DisplayName("중복된 이름인 경우 예외를 던진다.")
    public void throwsExceptionWithDuplicatedName() {
        // given
        LineSeries series = new LineSeries(List.of(LineFixture.getLineAb()));
        // when
        Line line = new Line("분당선", "color3", 0);
        // then
        assertThatExceptionOfType(RowDuplicatedException.class)
                .isThrownBy(() -> series.add(line))
                .withMessageContaining("분당선 는 이미 존재하는 노선 이름입니다.");
    }

    @Test
    @DisplayName("노선을 추가한다")
    public void addLine() {
        // given
        LineSeries series = new LineSeries(List.of(LineFixture.getLineAb()));

        // when
        series.add(new Line("새로운 역", "새로운 색", 0));

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
                .isThrownBy(() -> series.delete(3L))
                .withMessageContaining("3 의 ID에 해당하는 노선이 없습니다.");
    }

    @Test
    @DisplayName("노선을 업데이트한다")
    public void updateLine() {
        // given
        LineSeries series = new LineSeries(List.of(LineFixture.getLineAb()));

        // when
        series.update(new Line(LineFixture.getLineAb().getId(), "뉴네임", "뉴컬러", 0));

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
                .isThrownBy(() -> series.update(new Line(999L, "뉴네임", "뉴컬러", 0)))
                .withMessageContaining("해당하는 노선을 찾을 수 없습니다.");

    }

    @Test
    @DisplayName("주어진 id 중 가장 높은 추가요금을 반환한다.")
    void findMaxExtraFare() {
        //given
        LineSeries series = new LineSeries(List.of(
                new Line(1L, "1호선", "파란색", 0),
                new Line(2L, "2호선", "초록색", 50),
                new Line(3L, "3호선", "주황색", 100)
        ));

        //when
        int extraFare = series.findMaxExtraFare(Set.of(1L, 2L));

        //then
        assertThat(extraFare).isEqualTo(50);
    }
}
