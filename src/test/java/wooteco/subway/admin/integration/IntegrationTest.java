package wooteco.subway.admin.integration;


import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.repository.StationRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class IntegrationTest {

    @Autowired
    StationRepository stationRepository;

    @Test
    void name() {
        Station station1 = stationRepository.save(new Station("강남역"));
        Station station2 = stationRepository.save(new Station("역삼역"));

        List<String> result = stationRepository
            .findAllNameById(Arrays.asList(station1.getId(), station2.getId()));
        Assertions.assertThat(result).hasSize(2);

    }
}
