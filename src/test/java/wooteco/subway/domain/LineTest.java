package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

	@DisplayName("id가 같은지 확인한다.")
	@Test
	void isSameId() {
		Line line = new Line(1L, "신분당선", "bh-red-600");
		assertThat(line.isSameId(1L)).isTrue();
	}

	@DisplayName("이름이 같은지 확인한다.")
	@Test
	void isSameName() {
		Line line = new Line(1L, "신분당선", "bh-red-600");
		assertThat(line.isSameName("신분당선")).isTrue();
	}

	@DisplayName("음수로 추가 요금을 설정하지는 못한다.")
	@Test
	void validateExtraFare() {
		assertThatThrownBy(() ->
			new Line(1L, "신분당선", "red", -1, new ArrayList<>()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("추가 요금은 0언 이상이어야 합니다.");
	}
}
