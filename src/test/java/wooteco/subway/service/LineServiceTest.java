package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.section.SectionCreationRequest;
import wooteco.subway.exception.line.DuplicateLineException;
import wooteco.subway.exception.line.NoSuchLineException;

class LineServiceTest extends ServiceTest {

    private static final String LINE_NAME = "7호선";
    private static final String LINE_COLOR = "bg-red-600";

    @InjectMocks
    private LineService lineService;

    @Mock
    private SectionService sectionService;

    private Station upStation;
    private Station downStation;
    private LineRequest request;

    @BeforeEach
    void setUpData() {
        upStation = new Station(1L, "선릉역");
        downStation = new Station(2L, "삼성역");
        request = new LineRequest(LINE_NAME, LINE_COLOR, upStation.getId(), downStation.getId(), 10, 0);
    }

    @Test
    @DisplayName("노선과 구간을 생성한다.")
    void Create_WithSection_Success() {
        // given
        final Line expected = new Line(1L, LINE_NAME, LINE_COLOR);
        given(lineDao.insert(any(Line.class)))
                .willReturn(Optional.of(expected));

        final Section section = new Section(1L, expected, upStation, downStation, new Distance(10));
        given(sectionService.insert(any(SectionCreationRequest.class)))
                .willReturn(section);

        // when
        final Line actual = lineService.create(request);

        // then
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getColor()).isEqualTo(expected.getColor());

        final List<String> expectedStationNames = Stream.of(upStation, downStation)
                .map(Station::getName)
                .collect(Collectors.toList());
        final List<String> actualStationNames = actual.getSections()
                .toStation()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        assertThat(actualStationNames).isEqualTo(expectedStationNames);
    }

    @Test
    @DisplayName("저장하려는 노선의 이름이 중복되면 예외를 던진다.")
    void Create_DuplicateName_ExceptionThrown() {
        // given
        given(lineDao.insert(any(Line.class)))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> lineService.create(request))
                .isInstanceOf(DuplicateLineException.class);
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void FindAll() {
        // given
        final Line line1 = new Line(1L, LINE_NAME, LINE_COLOR);
        final Sections sectionsOfLine1 = new Sections(List.of(
                new Section(line1, upStation, downStation, new Distance(10))
        ));

        final Line line2 = new Line(2L, "5호선", "bg-blue-600");
        final Station upStation2 = new Station(3L, "왕십리역");
        final Station downStation2 = new Station(4L, "답십리역");
        final Sections sectionsOfLine2 = new Sections(List.of(
                new Section(line2, upStation2, downStation2, new Distance(10))
        ));
        final List<Line> expected = List.of(
                line1.addSections(sectionsOfLine1),
                line2.addSections(sectionsOfLine2)
        );
        given(lineDao.findAll())
                .willReturn(expected);

        // when
        final List<Line> actual = lineService.findAll();

        // then
        assertThat(actual).hasSameSizeAs(expected);

        final Line actualLine1 = actual.get(0);
        assertThat(actualLine1.getName()).isEqualTo(LINE_NAME);

        final List<String> actualStationNames1 = actualLine1.getSections()
                .toStation()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        assertThat(actualStationNames1).containsExactly(upStation.getName(), downStation.getName());

        final Line actualLine = actual.get(1);
        assertThat(actualLine.getName()).isEqualTo("5호선");
        final List<String> actualStationNames2 = actualLine.getSections()
                .toStation()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        assertThat(actualStationNames2).containsExactly("왕십리역", "답십리역");
    }

    @Test
    @DisplayName("id에 해당하는 노선을 조회한다.")
    void FindById() {
        // given
        final long id = 1L;
        final Line expected = new Line(id, LINE_NAME, LINE_COLOR);
        final Sections sections = new Sections(List.of(
                new Section(1L, expected, upStation, downStation, new Distance(10))
        ));

        given(lineDao.findById(any(Long.class)))
                .willReturn(Optional.of(expected.addSections(sections)));

        // when
        final Line actual = lineService.findById(id);

        // then
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getColor()).isEqualTo(expected.getColor());

        final List<String> actualStationNames = actual.getSections()
                .toStation()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        final List<String> expectedStationNames = List.of(upStation.getName(), downStation.getName());
        assertThat(actualStationNames).isEqualTo(expectedStationNames);
    }

    @Test
    @DisplayName("id에 해당하는 노선이 존재하지 않으면 예외를 던진다.")
    void FindById_NotExistId_ExceptionThrown() {
        // given
        final long id = 1L;
        given(lineDao.findById(id))
                .willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> lineService.findById(id))
                .isInstanceOf(NoSuchLineException.class);
    }

    @Test
    @DisplayName("id에 해당하는 노선 정보를 수정한다.")
    void UpdateById() {
        // given
        final long id = 1L;

        final Line existLine = new Line("xxx", "xx-x-xx");
        given(lineDao.findById(any(Long.class)))
                .willReturn(Optional.of(existLine));

        final Line updateLine = new Line(LINE_NAME, LINE_COLOR);
        given(lineDao.updateById(id, updateLine))
                .willReturn(Optional.of(updateLine));

        // then
        assertThatCode(() -> lineService.updateById(id, request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("업데이트하려는 노선이 존재하지 않으면 예외를 던진다.")
    void UpdateById_NotExistId_ExceptionThrown() {
        // given
        given(lineDao.findById(any(Long.class)))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> lineService.updateById(1L, request))
                .isInstanceOf(NoSuchLineException.class);
    }

    @Test
    @DisplayName("업데이트하려는 이름이 중복되면 예외를 던진다.")
    void UpdateById_DuplicateName_ExceptionThrown() {
        // given
        final long id = 1L;

        final Line existLine = new Line("xxx", "xx-x-xx");
        given(lineDao.findById(any(Long.class)))
                .willReturn(Optional.of(existLine));

        final Line updateLine = new Line(LINE_NAME, LINE_COLOR);
        given(lineDao.updateById(id, updateLine))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> lineService.updateById(id, request))
                .isInstanceOf(DuplicateLineException.class);
    }

    @Test
    @DisplayName("id에 해당하는 노선을 삭제한다.")
    void DeleteById() {
        // given
        final long id = 1L;
        given(lineDao.deleteById(id))
                .willReturn(1);

        // then
        assertThatCode(() -> lineService.deleteById(id))
                .doesNotThrowAnyException();
    }
}