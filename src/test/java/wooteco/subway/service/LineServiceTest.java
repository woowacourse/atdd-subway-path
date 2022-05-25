package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.CreateLineRequest;
import wooteco.subway.dto.request.UpdateLineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class LineServiceTest extends DatabaseUsageTest {

    private static final Station STATION1 = new Station(1L, "강남역");
    private static final Station STATION2 = new Station(2L, "선릉역");
    private static final Station STATION3 = new Station(3L, "잠실역");

    @Autowired
    private LineService service;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @DisplayName("findAll 및 find 메서드는 데이터를 조회한다")
    @Nested
    class FindMethodsTest {

        private final StationResponse STATION_RESPONSE_1 = new StationResponse(1L, "강남역");
        private final StationResponse STATION_RESPONSE_2 = new StationResponse(2L, "선릉역");
        private final StationResponse STATION_RESPONSE_3 = new StationResponse(3L, "잠실역");

        @BeforeEach
        void setup() {
            databaseFixtureUtils.saveStations(STATION1, STATION2, STATION3);
            databaseFixtureUtils.saveLine("1호선", "색깔", 1000);
            databaseFixtureUtils.saveLine("2호선", "색깔2", 0);
            databaseFixtureUtils.saveSection(2L, STATION3, STATION1);
            databaseFixtureUtils.saveSection(2L, STATION1, STATION2);
            databaseFixtureUtils.saveSection(1L, STATION1, STATION3);
        }

        @Test
        void findAll_메서드는_모든_데이터를_노선_id_순서대로_조회() {
            List<LineResponse> actual = service.findAll();

            LineResponse expectedLine1 = new LineResponse(1L, "1호선", "색깔", 1000,
                    List.of(STATION_RESPONSE_1, STATION_RESPONSE_3));
            LineResponse expectedLine2 = new LineResponse(2L, "2호선", "색깔2", 0,
                    List.of(STATION_RESPONSE_3, STATION_RESPONSE_1, STATION_RESPONSE_2));
            List<LineResponse> expected = List.of(expectedLine1, expectedLine2);

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        void find_메서드는_구간_정보를_포함하여_특정_노선의_모든_지하철역_정보를_정렬하여_조회() {
            LineResponse actual = service.find(2L);

            LineResponse expected = new LineResponse(2L, "2호선", "색깔2", 0,
                    List.of(STATION_RESPONSE_3, STATION_RESPONSE_1, STATION_RESPONSE_2));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void find_메서드는_존재하지_않는_노선을_조회하려는_경우_예외_발생() {
            assertThatThrownBy(() -> service.find(99999L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @DisplayName("save 메서드는 데이터를 저장한다")
    @Nested
    class SaveTest {

        @Test
        void 유효한_입력인_경우_성공() {
            databaseFixtureUtils.saveStations(STATION1, STATION2);

            LineResponse actual = service.save(new CreateLineRequest(
                    "새로운 노선명", "색깔", 300, 1L, 2L, 10));
            LineResponse expected = new LineResponse(1L, "새로운 노선명", "색깔", 300,
                    List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "선릉역")));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 중복되는_노선명인_경우_예외발생() {
            databaseFixtureUtils.saveStations(STATION1, STATION2, STATION3);
            databaseFixtureUtils.saveLine("존재하는 노선명", "색깔");
            databaseFixtureUtils.saveSection(1L, STATION1, STATION2);

            CreateLineRequest duplicateLineNameRequest = new CreateLineRequest(
                    "존재하는 노선명", "색깔", 200, 1L, 3L, 10);
            assertThatThrownBy(() -> service.save(duplicateLineNameRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상행역을_입력한_경우_예외발생() {
            databaseFixtureUtils.saveStations(STATION1, STATION2);
            databaseFixtureUtils.saveLine("노선1", "색깔");
            databaseFixtureUtils.saveSection(1L, STATION1, STATION2);

            CreateLineRequest noneExistingUpStationRequest = new CreateLineRequest(
                    "유효 노선명", "유효한 색", 200, 999L, 1L, 10);
            assertThatThrownBy(() -> service.save(noneExistingUpStationRequest))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 존재하지_않는_하행역을_입력한_경우_예외발생() {
            databaseFixtureUtils.saveStations(STATION1, STATION2);
            databaseFixtureUtils.saveLine("노선1", "색깔");
            databaseFixtureUtils.saveSection(1L, STATION1, STATION2);

            CreateLineRequest noneExistingDownStationRequest = new CreateLineRequest(
                    "유효한 노선명", "유효한 색상", 200, 1L, 999L, 10);
            assertThatThrownBy(() -> service.save(noneExistingDownStationRequest))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 거리가_1이하인_경우_예외발생() {
            databaseFixtureUtils.saveStations(STATION1, STATION2);

            CreateLineRequest zeroDistanceRequest = new CreateLineRequest(
                    "유효한 노선명", "색깔", 200, 1L, 2L, 0);
            assertThatThrownBy(() -> service.save(zeroDistanceRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 추가비용이_0미만인_경우_예외발생() {
            databaseFixtureUtils.saveStations(STATION1, STATION2);

            CreateLineRequest negativeExtraFareRequest = new CreateLineRequest(
                    "유효한 노선명", "색깔", -1, 1L, 2L, 0);
            assertThatThrownBy(() -> service.save(negativeExtraFareRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("update 메서드는 데이터를 수정한다")
    @Nested
    class UpdateTest {

        @Test
        void 유효한_입력인_경우_성공() {
            databaseFixtureUtils.saveLine("노선1", "색깔", 100);

            service.update(1L, new UpdateLineRequest("수정된 노선명", "수정된 색깔", 300));
            Line actual = lineDao.findById(1L).get();
            Line expected = new Line(1L, "수정된 노선명", "수정된 색깔", 300);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 존재하지_않는_노선을_수정하려는_경우_예외발생() {
            UpdateLineRequest validValueRequest = new UpdateLineRequest(
                    "새로운 노선명", "새로운 색깔", 300);
            assertThatThrownBy(() -> service.update(999999L, validValueRequest))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 중복되는_노선명으로_수정하려는_경우_예외발생() {
            databaseFixtureUtils.saveLine("존재하는 노선명", "색깔");
            databaseFixtureUtils.saveLine("현재 노선명", "색깔");

            UpdateLineRequest duplicateLineNameRequest = new UpdateLineRequest(
                    "존재하는 노선명", "새로운 색깔", 300);
            assertThatThrownBy(() -> service.update(2L, duplicateLineNameRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 추가비용이_0미만인_경우_예외발생() {
            databaseFixtureUtils.saveLine("현재 노선명", "색깔");

            UpdateLineRequest negativeExtraFareRequest = new UpdateLineRequest(
                    "유효한 노선명", "색깔", -1);
            assertThatThrownBy(() -> service.update(1L, negativeExtraFareRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("delete 메서드는 노선과 모든 구간 데이터를 삭제한다")
    @Nested
    class DeleteTest {

        @Test
        void 존재하는_데이터의_id가_입력된_경우_삭제성공() {
            databaseFixtureUtils.saveStations(STATION1, STATION2, STATION3);
            databaseFixtureUtils.saveLine("존재하는 노선", "색깔");
            databaseFixtureUtils.saveSection(1L, STATION1, STATION2);
            databaseFixtureUtils.saveSection(1L, STATION2, STATION3);

            service.delete(1L);
            boolean lineNotFound = lineDao.findById(1L).isEmpty();
            List<?> sectionsConnectedToLine = sectionDao.findAllByLineId(1L);

            assertThat(lineNotFound).isTrue();
            assertThat(sectionsConnectedToLine).isEmpty();
        }

        @Test
        void 존재하지_않는_데이터의_id가_입력된_경우_예외발생() {
            assertThatThrownBy(() -> service.delete(99999L))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
