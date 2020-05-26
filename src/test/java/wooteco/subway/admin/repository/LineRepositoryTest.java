package wooteco.subway.admin.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
public class LineRepositoryTest {
    @Autowired
    private LineRepository lineRepository;

    @DisplayName("구간이 저장되어있는 노선을 확인한다.")
    @Test
    void addLineStation() {
        // given : 구간이 추가된 노선이 있다.
        Line line = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30),
                5, "bg-green-500");
        Line persistLine = lineRepository.save(line);
        persistLine.addLineStation(new LineStation(null, 1L, 10, 10));
        persistLine.addLineStation(new LineStation(1L, 2L, 10, 10));

        // when : 노선을 저장한다.
        Line resultLine = lineRepository.save(persistLine);

        // then : 저장이 되었는지 확인한다.
        assertThat(resultLine.getStations()).hasSize(2);
    }
}
