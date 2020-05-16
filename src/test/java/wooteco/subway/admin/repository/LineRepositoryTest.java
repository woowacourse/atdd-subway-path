package wooteco.subway.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.domain.Line;

@DataJdbcTest
@Sql("/truncate.sql")
class LineRepositoryTest {

	@Autowired
	private LineRepository lineRepository;

	@DisplayName("Line CRUD 테스트")
	@Test
	void name() {
		//C
		Line 신분당선 = lineRepository
			.save(new Line("신분당선", "bg-red-600", LocalTime.of(8, 0), LocalTime.of(8, 0), 10));
		assertThat(신분당선.getId()).isNotNull();

		//R
		Optional<Line> maybeLine = lineRepository.findById(신분당선.getId());
		assertThat(maybeLine.isPresent()).isTrue();

		//U
		Line 분당선 = new Line("분당선", "bg-yellow-600", LocalTime.of(8, 0), LocalTime.of(8, 0), 10);
		신분당선.update(분당선);
		Line actual = lineRepository.save(신분당선);
		assertThat(actual.getName()).isEqualTo("분당선");

		//D
		lineRepository.delete(신분당선);
		maybeLine = lineRepository.findById(신분당선.getId());

		assertThat(maybeLine.isPresent()).isFalse();
	}

}