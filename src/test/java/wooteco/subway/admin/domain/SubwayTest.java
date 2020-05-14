package wooteco.subway.admin.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayTest {

	@DisplayName("Subway 객체를 생성한다.")
	@Test
	void createSubway_WhenNormalCase_ReturnInstance() {
		List<Line> lines = new ArrayList<>();
		Subway subway = new Subway(lines);
		assertThat(subway).isInstanceOf(Subway.class);
		assertThat(subway).isNotNull();
	}

	@DisplayName("Subway 객체를 생성시 null 값이 주입되면 예외처리한다.")
	@Test
	void createSubway_WhenNull_ThrowException() {
		List<Line> lines = null;
		assertThatThrownBy(() -> new Subway(lines)).isInstanceOf(IllegalArgumentException.class).hasMessage("List<Line>이 null일 수 없습니다.");
	}
}