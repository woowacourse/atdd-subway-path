package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.line.LineResponse;

@JdbcTest
class LineDaoTest {

    private static final String LINE_NAME = "테스트1호선";
    private static final String LINE_COLOR = "테스트1색";
    private static final int LINE_EXTRA_FARE = 100;
    private LineDao lineDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineResponse lineResponse;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        lineResponse = lineDao.create(new LineRequest(LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE));
    }

    @Test
    void create() {
        //given
        var id = findLines().get(0).getId();

        //then
        assertAll(
                () -> assertThat(lineResponse.getId()).isEqualTo(id),
                () -> assertThat(lineResponse.getName()).isEqualTo(LINE_NAME),
                () -> assertThat(lineResponse.getColor()).isEqualTo(LINE_COLOR),
                () -> assertThat(lineResponse.getExtraFare()).isEqualTo(LINE_EXTRA_FARE)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"테스트1호선,테스트2색", "테스트2호선,테스트1색"})
    void createByInvalidInformation(String name, String color) {
        //given
        var lineRequest = new LineRequest(name, color, LINE_EXTRA_FARE);

        //when
        assertThatThrownBy(() -> lineDao.create(lineRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 존재하는 노선 정보 입니다.");
    }

    @Test
    void findById() {
        //given
        var id = lineResponse.getId();

        //when
        var actual = lineDao.find(id);

        //then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(LINE_NAME),
                () -> assertThat(actual.getColor()).isEqualTo(LINE_COLOR),
                () -> assertThat(actual.getExtraFare()).isEqualTo(LINE_EXTRA_FARE)
        );
    }

    @Test
    void findByInvalidId() {
        //given
        var invalidId = -1L;

        //when
        assertThatThrownBy(() -> lineDao.find(invalidId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 해당 노선이 존재하지 않습니다.");
    }

    @Test
    void findAll() {
        //given
        var newName = "테스트2호선";
        var newColor = "테스트2색";
        var newExtraFare = 200;
        lineDao.create(new LineRequest(newName, newColor, newExtraFare));

        //when
        var lines = lineDao.findAll();

        //then
        var names = lines.stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());
        var colors = lines.stream()
                .map(LineResponse::getColor)
                .collect(Collectors.toList());
        var extraFares = lines.stream()
                .map(LineResponse::getExtraFare)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(names).containsExactly(LINE_NAME, newName),
                () -> assertThat(colors).containsExactly(LINE_COLOR, newColor),
                () -> assertThat(extraFares).containsExactly(LINE_EXTRA_FARE, newExtraFare)
        );
    }

    @Test
    void update() {
        //given
        var id = lineResponse.getId();
        var newName = "테스트2호선";
        var newColor = "테스트2색";
        var newExtraFare = 200;

        //when
        lineDao.update(id, new LineRequest(newName, newColor, newExtraFare));

        //then
        var actual = findLines().get(0);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(id),
                () -> assertThat(actual.getName()).isEqualTo(newName),
                () -> assertThat(actual.getColor()).isEqualTo(newColor),
                () -> assertThat(actual.getExtraFare()).isEqualTo(newExtraFare)
        );
    }

    private List<LineResponse> findLines() {
        var sql = "SELECT * FROM line";

        RowMapper<LineResponse> rowMapper = (rs, rowNum) -> {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            var color = rs.getString("color");
            var extraFare = rs.getInt("extraFare");

            return new LineResponse(id, name, color, extraFare);
        };

        return jdbcTemplate.query(sql, rowMapper);
    }

    @ParameterizedTest
    @CsvSource(value = {"테스트1호선,테스트2색", "테스트2호선,테스트1색"})
    void updateByInvalidInformation(String name, String color) {
        //given
        var newName = "테스트2호선";
        var newColor = "테스트2색";
        var newExtraFare = 200;
        var lineResponse = lineDao.create(new LineRequest(newName, newColor, newExtraFare));
        var id = lineResponse.getId();
        var lineRequest = new LineRequest(name, color, LINE_EXTRA_FARE);

        //when
        assertThatThrownBy(() -> lineDao.update(id, lineRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 존재하는 노선 정보 입니다.");
    }

    @Test
    void updateByInvalidId() {
        //given
        var invalidId = -1L;
        var newName = "테스트2호선";
        var newColor = "테스트2색";
        var newExtraFare = 200;
        var lineRequest = new LineRequest(newName, newColor, newExtraFare);

        //when
        assertThatThrownBy(() -> lineDao.update(invalidId, lineRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 해당 노선이 존재하지 않습니다.");
    }

    @Test
    void delete() {
        //given
        var id = lineResponse.getId();

        //when
        lineDao.deleteById(id);

        //then
        assertThat(findLines().isEmpty()).isTrue();
    }

    @Test
    void deleteByInvalidId() {
        //given
        var invalidId = -1L;

        //when
        assertThatThrownBy(() -> lineDao.deleteById(invalidId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 해당 노선이 존재하지 않습니다.");
    }
}
