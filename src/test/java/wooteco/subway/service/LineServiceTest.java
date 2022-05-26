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
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.CreateLineRequest;
import wooteco.subway.dto.request.UpdateLineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class LineServiceTest extends DatabaseUsageTest {

    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 선릉역 = new Station(2L, "선릉역");
    private static final Station 잠실역 = new Station(3L, "잠실역");

    @Autowired
    private LineService service;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @DisplayName("findAll 및 find 메서드는 데이터를 조회한다")
    @Nested
    class FindMethodsTest {

        private final StationResponse 강남역_응답 = new StationResponse(1L, "강남역");
        private final StationResponse 선릉역_응답 = new StationResponse(2L, "선릉역");
        private final StationResponse 잠실역_응답 = new StationResponse(3L, "잠실역");

        @BeforeEach
        void setup() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("1호선", "색깔", 1000);
            saveLineTestFixture("2호선", "색깔2", 0);
            saveSectionTestFixture(2L, 잠실역, 강남역, 10);
            saveSectionTestFixture(2L, 강남역, 선릉역, 10);
            saveSectionTestFixture(1L, 강남역, 잠실역, 10);
        }

        @Test
        void findAll_메서드는_모든_데이터를_노선_id_순서대로_조회() {
            List<LineResponse> actual = service.findAll();

            LineResponse expectedLine1 = new LineResponse(1L, "1호선", "색깔", 1000,
                    List.of(강남역_응답, 잠실역_응답));
            LineResponse expectedLine2 = new LineResponse(2L, "2호선", "색깔2", 0,
                    List.of(잠실역_응답, 강남역_응답, 선릉역_응답));
            List<LineResponse> expected = List.of(expectedLine1, expectedLine2);

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        void find_메서드는_구간_정보를_포함하여_특정_노선의_모든_지하철역_정보를_정렬하여_조회() {
            LineResponse actual = service.find(2L);

            LineResponse expected = new LineResponse(2L, "2호선", "색깔2", 0,
                    List.of(잠실역_응답, 강남역_응답, 선릉역_응답));

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
            databaseFixtureUtils.saveStations(강남역, 선릉역);

            LineResponse actual = service.save(generateCreatLineRequest(
                    "새로운 노선명", "색깔", 300, 강남역, 선릉역, 10));
            LineResponse expected = new LineResponse(1L, "새로운 노선명", "색깔", 300,
                    List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "선릉역")));

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 중복되는_노선명인_경우_예외발생() {
            String 존재하는_노선명 = "존재하는 노선명";
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture(존재하는_노선명, "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);

            CreateLineRequest duplicateLineNameRequest = generateCreatLineRequest(
                    존재하는_노선명, "색깔", 200, 강남역, 잠실역, 10);
            assertThatThrownBy(() -> service.save(duplicateLineNameRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상행역을_입력한_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            saveLineTestFixture("노선1", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);
            Station 저장되지_않은_역 = new Station("저장되지 않은 역");

            CreateLineRequest noneExistingUpStationRequest = generateCreatLineRequest(
                    "유효 노선명", "유효한 색", 200, 저장되지_않은_역, 강남역, 10);
            assertThatThrownBy(() -> service.save(noneExistingUpStationRequest))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 존재하지_않는_하행역을_입력한_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            saveLineTestFixture("노선1", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);
            Station 저장되지_않은_역 = new Station("저장되지 않은 역");

            CreateLineRequest noneExistingDownStationRequest = generateCreatLineRequest(
                    "유효한 노선명", "유효한 색상", 200, 선릉역, 저장되지_않은_역, 10);
            assertThatThrownBy(() -> service.save(noneExistingDownStationRequest))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 거리가_1이하인_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);

            CreateLineRequest zeroDistanceRequest =generateCreatLineRequest(
                    "유효한 노선명", "색깔", 200, 강남역, 선릉역, 0);
            assertThatThrownBy(() -> service.save(zeroDistanceRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 추가비용이_0미만인_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);

            CreateLineRequest negativeExtraFareRequest = generateCreatLineRequest(
                    "유효한 노선명", "색깔", -1, 강남역, 선릉역, 0);
            assertThatThrownBy(() -> service.save(negativeExtraFareRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private CreateLineRequest generateCreatLineRequest(String name,
                                                           String color,
                                                           int extraFare,
                                                           Station upStation,
                                                           Station downStation,
                                                           int distance) {
            CreateLineRequest request = new CreateLineRequest();
            request.setName(name);
            request.setColor(color);
            request.setExtraFare(extraFare);
            request.setUpStationId(upStation.getId());
            request.setDownStationId(downStation.getId());
            request.setDistance(distance);
            return request;
        }
    }

    @DisplayName("update 메서드는 데이터를 수정한다")
    @Nested
    class UpdateTest {

        @Test
        void 유효한_입력인_경우_성공() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            saveLineTestFixture("노선1", "색깔", 100);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);

            service.update(1L, generateUpdateLineRequest("수정된 노선명", "수정된 색깔", 300));
            LineEntity actual = lineDao.findById(1L).get();
            LineEntity expected = new LineEntity(1L, "수정된 노선명", "수정된 색깔", 300);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 존재하지_않는_노선을_수정하려는_경우_예외발생() {
            UpdateLineRequest validValueRequest = generateUpdateLineRequest(
                    "새로운 노선명", "새로운 색깔", 300);
            assertThatThrownBy(() -> service.update(999999L, validValueRequest))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 중복되는_노선명으로_수정하려는_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("존재하는 노선명", "색깔", 0);
            saveLineTestFixture("현재 노선명", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);
            saveSectionTestFixture(2L, 강남역, 잠실역, 10);

            UpdateLineRequest duplicateLineNameRequest = generateUpdateLineRequest(
                    "존재하는 노선명", "새로운 색깔", 300);
            assertThatThrownBy(() -> service.update(2L, duplicateLineNameRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 추가비용이_0미만인_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            saveLineTestFixture("현재 노선명", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);

            UpdateLineRequest negativeExtraFareRequest = generateUpdateLineRequest(
                    "유효한 노선명", "색깔", -1);
            assertThatThrownBy(() -> service.update(1L, negativeExtraFareRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private UpdateLineRequest generateUpdateLineRequest(String name, String color, int extraFare) {
            UpdateLineRequest request = new UpdateLineRequest();
            request.setName(name);
            request.setColor(color);
            request.setExtraFare(extraFare);
            return request;
        }
    }

    @DisplayName("delete 메서드는 노선과 모든 구간 데이터를 삭제한다")
    @Nested
    class DeleteTest {

        @Test
        void 존재하는_데이터의_id가_입력된_경우_삭제성공() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("존재하는 노선", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);
            saveSectionTestFixture(1L, 선릉역, 잠실역, 10);

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

    private void saveLineTestFixture(String name, String color, int extraFare) {
        databaseFixtureUtils.saveLines(new LineEntity(name, color, extraFare));
    }

    private void saveSectionTestFixture(Long lineId, Station upStation, Station downStation, int distance) {
        databaseFixtureUtils.saveSections(new Section(lineId, upStation,downStation, distance));
    }
}
