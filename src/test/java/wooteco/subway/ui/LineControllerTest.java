package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dto.LineUpdateRequest;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.LineResponse;

@SpringBootTest
@Sql("classpath:lineInit.sql")
@Transactional
class LineControllerTest {

    @Autowired
    LineController lineController;

    @Test
    @DisplayName("노선 생성하는 기능")
    void createLine() {
        ResponseEntity<LineResponse> 팔호선_생성 = lineController.createLine(
                new LineRequest("8호선", "blue", 1L, 2L, 10, 100L));
        assertThat(팔호선_생성.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("중복된 이름으로 노선 생성시 예외")
    void createLine_duplicated() {
        assertThatThrownBy(() -> lineController.createLine(new LineRequest("7호선", "blue", 1L, 2L, 100)))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("등록된 노선 조회")
    void showLines() {
        List<LineResponse> lineResponses = lineController.showLines();
        assertThat(lineResponses).hasSize(1);
    }

    @Test
    @DisplayName("등록된 특정 노선 조회")
    void showLine() {
        LineResponse response = lineController.showLine(1L);
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("7호선");
    }

    @Test
    @DisplayName("특정 노선 수정")
    void updateLine() {
        ResponseEntity<Void> updatedResponse = lineController.updateLine(1L, new LineUpdateRequest("8호선", "red", 100L));
        assertThat(updatedResponse.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("존재하지 않은 노선 수정할 경우 예외")
    void updateLine_invalidLine() {
        assertThatThrownBy(() -> lineController.updateLine(2L, new LineUpdateRequest("8호선", "red", 100L)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("특정 노선 삭제하는 기능")
    void deleteLine() {
        ResponseEntity<Void> 삭제_응답 = lineController.deleteLine(1L);
        assertThat(삭제_응답.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("존재하지 않는 노선 삭제하려하면 예외")
    void deleteLine_invalidLine() {
        assertThatThrownBy(() -> lineController.deleteLine(2L))
                .isInstanceOf(NotFoundException.class);
    }
}
