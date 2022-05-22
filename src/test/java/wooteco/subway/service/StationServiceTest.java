package wooteco.subway.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;

@SpringBootTest
@Sql("classpath:schema.sql")
class StationServiceTest {

    @Autowired
    private StationService service;

    @Test
    @DisplayName("역을 저장한다.")
    public void saveStation() {
        // given
        StationRequest request = new StationRequest("역");
        // when
        final StationResponse save = service.save(request);
        // then
        assertThat(save.getId()).isNotNull();
    }

    @Test
    @DisplayName("모든 역을 조회한다")
    public void findAllStations() {
        // given
        service.save(new StationRequest("A역"));
        service.save(new StationRequest("B역"));

        // when
        final List<StationResponse> responses = service.findAll();

        // then
        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("역을 삭제한다.")
    public void deleteStation() {
        // given
        final Long savedId = service.save(new StationRequest("A역")).getId();

        // when
        service.deleteOne(savedId);

        // then
        assertThat(service.findAll()).hasSize(0);
    }
}