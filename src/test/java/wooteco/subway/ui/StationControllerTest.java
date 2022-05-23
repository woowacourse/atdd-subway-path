package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.StationEntity;
import wooteco.subway.service.dto.StationRequest;
import wooteco.subway.service.dto.StationResponse;

@SpringBootTest
@Transactional
class StationControllerTest {

    @Autowired
    StationController stationController;

    @Autowired
    StationDao stationDao;

    @Test
    @DisplayName("역 생성한다.")
    void createStation() {
        ResponseEntity<StationResponse> 노원역 = stationController.createStation(new StationRequest("노원역"));
        assertThat(노원역.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 역 생성하면 예외")
    void createStation_duplicated() {
        // given
        stationDao.save(new StationEntity(null, "노원역"));

        // then
        assertThatThrownBy(() -> stationController.createStation(new StationRequest("노원역")))
                .isInstanceOf(DomainException.class);
    }

    @ParameterizedTest
    @DisplayName("빈 이름으로 역 생성할 경우 예외")
    @EmptySource
    void createStation_empty(String name) {
        assertThatThrownBy(() -> stationController.createStation(new StationRequest(name)))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("저장된 역을 모두 조회")
    void showStations() {
        // given
        stationDao.save(new StationEntity(null, "노원역"));
        stationDao.save(new StationEntity(null, "하계역"));

        // when
        List<StationResponse> stationResponses = stationController.showStations();

        // then
        assertThat(stationResponses).hasSize(2);
    }

    @Test
    @DisplayName("저장된 역을 제거")
    void deleteStation() {
        // given
        StationEntity 노원역 = stationDao.save(new StationEntity(null, "노원역"));

        // when
        ResponseEntity<Void> 삭제_응답 = stationController.deleteStation(노원역.getId());

        // then
        assertThat(삭제_응답.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("저장되지 않은 역을 제거하려면 예외")
    void deleteStation_invalid() {
        assertThatThrownBy(() -> stationController.deleteStation(1L))
                .isInstanceOf(NotFoundException.class);
    }
}
