package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import wooteco.subway.admin.dto.StationResponse;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 관리한다")
    @TestFactory
    Stream<DynamicTest> manageStation() {
        return Stream.of(
                dynamicTest("역 추가", () -> {
                    createStation(STATION_NAME_KANGNAM);
                    createStation(STATION_NAME_YEOKSAM);
                    createStation(STATION_NAME_SEOLLEUNG);

                    List<StationResponse> stations = getStations();

                    assertThat(stations.size()).isEqualTo(3);
                }),
                dynamicTest("역 제거", () -> {
                    List<StationResponse> stations = getStations();
                    deleteStation(stations.get(0).getId());

                    List<StationResponse> stationsAfterDelete = getStations();

                    assertThat(stationsAfterDelete.size()).isEqualTo(2);
                })
        );
    }
}
