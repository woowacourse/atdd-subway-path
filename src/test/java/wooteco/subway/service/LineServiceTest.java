package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.reopository.LineRepository;
import wooteco.subway.reopository.SectionRepository;
import wooteco.subway.reopository.StationRepository;

@SpringBootTest
class LineServiceTest {

    @Autowired
    private LineService lineService;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("TRUNCATE table line");
        jdbcTemplate.update("TRUNCATE table station");
        jdbcTemplate.update("TRUNCATE table section");
    }

    @DisplayName("없는 역으로 구간 생성시 에러가 발생한다.")
    @Test
    void create_with_none_station_false() {
        // given
        Long 미르역 = stationRepository.save(new Station("미르역"));
        Station 없는역 = new Station(100L, "없는역");
        Long 우테코노선 = lineRepository.save(new Line("우테코노선", "노랑"));

        // when, then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> lineService.createSection(우테코노선,
                        new SectionRequest(미르역, 없는역.getId(), 100)));
    }

    @DisplayName("맨 앞에 구간을 추가한다.")
    @Test
    void create_front_add_section() {
        // given
        Long 미르역 = stationRepository.save(new Station("미르역"));
        Long 수달역 = stationRepository.save(new Station("수달역"));
        Long 호호역 = stationRepository.save(new Station("호호역"));
        Line 우테코노선 = new Line("우테코노선", "노랑");

        LineResponse lineResponse = lineService.create(
                new LineRequest(우테코노선.getName(), 우테코노선.getColor(), 미르역, 수달역, 100));
        // when
        lineService.createSection(lineResponse.getId(),
                new SectionRequest(호호역, 미르역, 100));

        // then
        assertThat(lineService.showById(lineResponse.getId()).getStations().size()).isEqualTo(3);
    }

    @DisplayName("맨 뒤에 구간을 추가한다.")
    @Test
    void create_back_add_section() {
        // given
        Long 미르역 = stationRepository.save(new Station("미르역"));
        Long 수달역 = stationRepository.save(new Station("수달역"));
        Long 호호역 = stationRepository.save(new Station("호호역"));
        Line 우테코노선 = new Line("우테코노선", "노랑");

        LineRequest request = new LineRequest(우테코노선.getName(), 우테코노선.getColor(), 미르역, 수달역, 100);

        LineResponse lineResponse = lineService.create(request);
        // when
        lineService.createSection(lineResponse.getId(),
                new SectionRequest(수달역, 호호역, 100));

        // then
        assertThat(lineService.showById(lineResponse.getId()).getStations().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("구간 사이에 역을 추가한다.")
    void addSectionBetweenSection() {
        Long 미르역 = stationRepository.save(new Station("미르역"));
        Long 수달역 = stationRepository.save(new Station("수달역"));
        Long 호호역 = stationRepository.save(new Station("호호역"));
        Line 우테코노선 = new Line("우테코노선", "노랑");

        LineRequest request = new LineRequest(우테코노선.getName(), 우테코노선.getColor(), 미르역, 수달역, 100);
        LineResponse lineResponse = lineService.create(request);
        // when
        lineService.createSection(lineResponse.getId(),
                new SectionRequest(미르역, 호호역, 40));

        // then
        List<Section> result = sectionRepository.findByLineId(lineResponse.getId());
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDistance()).isEqualTo(40);
        assertThat(result.get(1).getDistance()).isEqualTo(60);
    }

    @Test
    @DisplayName("구간 사이에 역을 추가한다. 강남-양재-광교 -> 강남-양재-판교-광교")
    void addSectionInSection() {
        Long 강남 = stationRepository.save(new Station("강남"));
        Long 양재 = stationRepository.save(new Station("양재"));
        Long 광교 = stationRepository.save(new Station("광교"));
        Long 판교 = stationRepository.save(new Station("판교"));
        Long 신분당선 = lineRepository.save(new Line("신분당선", "red"));
        sectionRepository.save(
                new Section(lineRepository.findById(신분당선, "노선 못찾음"), stationRepository.findById(강남, "역 못찾음"),
                        stationRepository.findById(광교, "역 못찾음"), 10));
        SectionRequest sectionRequest1 = new SectionRequest(강남, 양재, 4);
        lineService.createSection(신분당선, sectionRequest1);

        SectionRequest sectionRequest2 = new SectionRequest(양재, 판교, 4);
        lineService.createSection(신분당선, sectionRequest2);

        List<Section> sections = sectionRepository.findByLineId(신분당선);
        Section 구간2 = findSectionEntity(stationRepository.findById(양재, "역 못찾음"), sections);
        Section 구간3 = findSectionEntity(stationRepository.findById(판교, "역 못찾음"), sections);
        assertThat(구간2.getDownStation().getId()).isEqualTo(판교);
        assertThat(구간2.getDistance()).isEqualTo(4);
        assertThat(구간3.getDownStation().getId()).isEqualTo(광교);
        assertThat(구간3.getDistance()).isEqualTo(2);
    }

    private Section findSectionEntity(Station startStation, List<Section> sections) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(startStation))
                .findFirst()
                .get();
    }

    @Test
    @DisplayName("맨 앞 역을 삭제한다. ")
    void deleteFrontStation() {
        Long 미르역 = stationRepository.save(new Station("미르역"));
        Long 수달역 = stationRepository.save(new Station("수달역"));
        Long 호호역 = stationRepository.save(new Station("호호역"));
        Line 우테코노선 = new Line("우테코노선", "노랑");
        LineResponse lineResponse = createTwoSection(미르역, 수달역, 호호역, 우테코노선);

        // when
        lineService.delete(lineResponse.getId(), 미르역);

        // then
        List<Section> result = sectionRepository.findByLineId(lineResponse.getId());
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("맨 뒤 역을 삭제한다. ")
    void deleteBackStation() {
        Long 미르역 = stationRepository.save(new Station("미르역"));
        Long 수달역 = stationRepository.save(new Station("수달역"));
        Long 호호역 = stationRepository.save(new Station("호호역"));
        Line 우테코노선 = new Line("우테코노선", "노랑");
        LineResponse lineResponse = createTwoSection(미르역, 수달역, 호호역, 우테코노선);

        // when
        lineService.delete(lineResponse.getId(), 호호역);

        // then
        List<Section> result = sectionRepository.findByLineId(lineResponse.getId());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUpStation().getId()).isEqualTo(미르역);
        assertThat(result.get(0).getDownStation().getId()).isEqualTo(수달역);
    }

    @Test
    @DisplayName("가운데 역을 삭제한다.")
    void deleteBetweenStation() {
        Long 미르역 = stationRepository.save(new Station("미르역"));
        Long 수달역 = stationRepository.save(new Station("수달역"));
        Long 호호역 = stationRepository.save(new Station("호호역"));
        Line 우테코노선 = new Line("우테코노선", "노랑");
        LineResponse lineResponse = createTwoSection(미르역, 수달역, 호호역, 우테코노선);

        // when
        lineService.delete(lineResponse.getId(), 수달역);

        // then
        List<Section> result = sectionRepository.findByLineId(lineResponse.getId());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUpStation().getId()).isEqualTo(미르역);
        assertThat(result.get(0).getDownStation().getId()).isEqualTo(호호역);
    }

    private LineResponse createTwoSection(Long 미르역, Long 수달역, Long 호호역, Line 우테코노선) {
        LineRequest request = new LineRequest(우테코노선.getName(), 우테코노선.getColor(), 미르역, 수달역, 100);
        LineResponse lineResponse = lineService.create(request);
        lineService.createSection(lineResponse.getId(),
                new SectionRequest(미르역, 호호역, 40));
        return lineResponse;
    }
}
