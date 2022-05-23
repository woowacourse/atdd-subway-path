package wooteco.subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.Infrastructure.line.LineDao;
import wooteco.subway.Infrastructure.line.MemoryLineDao;
import wooteco.subway.Infrastructure.section.MemorySectionDao;
import wooteco.subway.Infrastructure.section.SectionDao;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.constant.BlankArgumentException;
import wooteco.subway.exception.constant.DuplicateException;
import wooteco.subway.exception.constant.NotExistException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.utils.LineFixtures.신분당선;
import static wooteco.subway.utils.SectionFixture.샘플_구간;

public class LineServiceTest {

    private SectionDao sectionDao;
    private LineDao lineDao;
    private LineService lineService;

    @BeforeEach
    void setUp() {
        lineDao = new MemoryLineDao();
        sectionDao = new MemorySectionDao();
        lineService = new LineService(lineDao, sectionDao);
    }

    @DisplayName("노선 저장이 정상적으로 이루어져야 한다")
    @Test
    void saveLine() {
        Line saveLine = lineService.saveAndGet(신분당선, 샘플_구간);
        assertThat(lineDao.findById(saveLine.getId())).isNotEmpty();
    }

    @DisplayName("지하철 노선 빈 이름으로 저장하면 예외를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "     "})
    void saveLineWithEmptyName(String name) {
        assertThatThrownBy(() -> {
            Line line = new Line(name, "bg-red-600", 0);
            lineService.saveAndGet(line, 샘플_구간);
        })
                .isInstanceOf(BlankArgumentException.class);
    }

    @DisplayName("지하철 노선 빈 색깔로 저장하면 예외를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "     "})
    void saveLineWithEmptyColor(String color) {
        assertThatThrownBy(() -> {
            Line line = new Line("신분당선", color, 0);
            lineService.saveAndGet(line, 샘플_구간);
        })
                .isInstanceOf(BlankArgumentException.class);
    }

    @DisplayName("노선을 중복하여 저장하면 예외를 반환한다")
    @Test
    void saveByDuplicateName() {
        lineService.saveAndGet(신분당선, 샘플_구간);

        assertThatThrownBy(() -> lineService.saveAndGet(신분당선, 샘플_구간))
                .isInstanceOf(DuplicateException.class);
    }

    @DisplayName("존재하지 않는 지하철 노선 조회시 예외를 반환한다")
    @Test
    void showNotExistLine() {
        assertThatThrownBy(() -> lineService.findById(50L))
                .isInstanceOf(NotExistException.class);
    }

    @DisplayName("노선을 빈 이름으로 수정하면 예외를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "     "})
    void updateLineWithEmptyName(String name) {
        Line saveLine = lineService.saveAndGet(신분당선, 샘플_구간);

        assertThatThrownBy(() -> lineService.update(saveLine.getId(), name, "bg-red-600", 0))
                .isInstanceOf(BlankArgumentException.class);
    }

    @DisplayName("노선을 빈 색깔로 수정하면 예외를 반환한다")
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "     "})
    void updateLineWithEmptyColor(String color) {
        Line saveLine = lineService.saveAndGet(신분당선, 샘플_구간);

        assertThatThrownBy(() -> lineService.update(saveLine.getId(), "신분당선", color, 0))
                .isInstanceOf(BlankArgumentException.class);
    }

    @DisplayName("노선 정보 수정이 정상적으로 되는지 검증한다")
    @Test
    void updateLine() {
        Line saveLine = lineService.saveAndGet(신분당선, 샘플_구간);

        lineService.update(saveLine.getId(), "1호선", "bg-blue-600", 0);

        Line expectedLine = lineDao.findById(saveLine.getId()).orElseThrow();
        assertThat(expectedLine.getName()).isEqualTo("1호선");
        assertThat(expectedLine.getColor()).isEqualTo("bg-blue-600");
    }

    @DisplayName("존재하지 않는 노선 ID를 대상으로 수정한다")
    @Test
    void updateNotExistLine() {
        assertThatThrownBy(() -> lineService.update(50L, "1호선", "bg-red-600", 0))
                .isInstanceOf(NotExistException.class);
    }

    @DisplayName("존재하지 않는 지하철 노선을 삭제 시도시 예외를 반환한다")
    @Test
    void deleteNotExistLine() {
        assertThatThrownBy(() -> lineService.deleteById(50L))
                .isInstanceOf(NotExistException.class);
    }

    @DisplayName("지하철 노선을 삭제할 수 있는지 검증한다")
    @Test
    void deleteLine() {
        Line saveLine = lineService.saveAndGet(신분당선, 샘플_구간);

        lineService.deleteById(saveLine.getId());

        assertThat(lineDao.findById(saveLine.getId())).isEmpty();
    }
}
