package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
public class PathAcceptanceTest extends AcceptanceTest {

    private static final String JAMSIL_STATION_NAME = "잠실역";
    private static final String JAMSIL_SAENAE_STATION_NAME = "잠실새내역";
    private static final String OLYMPIC_STATION_NAME = "종합운동장역";
    private static final String SAMJEON_STATION_NAME = "삼전역";
    private static final String SEOKCHON_TOMB_STATION_NAME = "석촌고분역";
    private static final String SEOKCHON_STATION_NAME = "석촌역";
    private static final String SECOND_LINE_NAME = "2호선";
    private static final String EIGHT_LINE_NAME = "8호선";

    private LineResponse secondLineResponse;
    private StationResponse jamsilStationResponse;
    private StationResponse jamsilSaenaeResponse;
    private StationResponse olympicStationResponse;
    private LineResponse eighthLineResponse;
    private StationResponse samjeonStationResponse;
    private StationResponse seokchonTombStationResponse;
    private StationResponse seokchonStationResponse;

    private void initialize() {
        LineResponse secondLineResponse = createLine(SECOND_LINE_NAME);
        StationResponse jamsilStationResponse = createStation(JAMSIL_STATION_NAME);
        StationResponse jamsilSaenaeResponse = createStation(JAMSIL_SAENAE_STATION_NAME);
        StationResponse olympicStationResponse = createStation(OLYMPIC_STATION_NAME);
        addLineStation(secondLineResponse.getId(), null, jamsilStationResponse.getId(), 0, 0);
        addLineStation(secondLineResponse.getId(), jamsilStationResponse.getId(), jamsilSaenaeResponse.getId(), 1, 20);
        addLineStation(secondLineResponse.getId(), jamsilSaenaeResponse.getId(), olympicStationResponse.getId(), 1, 30);

        LineResponse eighthLineResponse = createLine(EIGHT_LINE_NAME);
        StationResponse samjeonStationResponse = createStation(SAMJEON_STATION_NAME);
        StationResponse seokchonTombStationResponse = createStation(SEOKCHON_TOMB_STATION_NAME);
        StationResponse seokchonStationResponse = createStation(SEOKCHON_STATION_NAME);
        addLineStation(eighthLineResponse.getId(), null, olympicStationResponse.getId(), 0, 0);
        addLineStation(eighthLineResponse.getId(), olympicStationResponse.getId(), samjeonStationResponse.getId(), 20, 1);
        addLineStation(eighthLineResponse.getId(), samjeonStationResponse.getId(), seokchonTombStationResponse.getId(), 30, 1);
        addLineStation(eighthLineResponse.getId(), seokchonTombStationResponse.getId(), seokchonStationResponse.getId(), 40, 1);
        addLineStation(eighthLineResponse.getId(), seokchonStationResponse.getId(), jamsilStationResponse.getId(), 50, 1);
    }

    @DisplayName("최소 거리 지하철 경로를 조회한다.")
    @Test
    void findPathsByDistance() {
        initialize();

        PathResponse response = getPath(JAMSIL_STATION_NAME, OLYMPIC_STATION_NAME, "DISTANCE");

        assertThat(response.getStations().get(0).getName()).isEqualTo(JAMSIL_STATION_NAME);
        assertThat(response.getStations().get(1).getName()).isEqualTo(JAMSIL_SAENAE_STATION_NAME);
        assertThat(response.getStations().get(2).getName()).isEqualTo(OLYMPIC_STATION_NAME);
        assertThat(response.getDistance()).isEqualTo(2);
        assertThat(response.getDuration()).isEqualTo(50);
    }

    @DisplayName("최소 시간 지하철 경로를 조회한다.")
    @Test
    void findPathsByDuration() {
        initialize();

        PathResponse response = getPath(JAMSIL_STATION_NAME, OLYMPIC_STATION_NAME, "DURATION");

        assertThat(response.getStations().get(0).getName()).isEqualTo(JAMSIL_STATION_NAME);
        assertThat(response.getStations().get(1).getName()).isEqualTo(SEOKCHON_STATION_NAME);
        assertThat(response.getStations().get(2).getName()).isEqualTo(SEOKCHON_TOMB_STATION_NAME);
        assertThat(response.getStations().get(3).getName()).isEqualTo(SAMJEON_STATION_NAME);
        assertThat(response.getStations().get(4).getName()).isEqualTo(OLYMPIC_STATION_NAME);
        assertThat(response.getDistance()).isEqualTo(140);
        assertThat(response.getDuration()).isEqualTo(4);
    }
}
