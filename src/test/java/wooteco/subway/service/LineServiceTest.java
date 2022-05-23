package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.exception.DataNotFoundException;
import wooteco.subway.exception.DuplicateNameException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private StationDao stationDao;

    @Mock
    private LineDao lineDao;

    @Mock
    private SectionDao sectionDao;

    @InjectMocks
    private LineService lineService;

    @DisplayName("새로운 노선과 첫 구간을 등록한다.")
    @Test
    void createLine() {
        final String lineName = "신분당선";
        final String lineColor = "bg-red-600";
        final Station station1 = new Station(1L, "선릉역");
        final Station station2 = new Station(2L, "강남역");
        final LineRequest lineRequest = new LineRequest(lineName, lineColor, 1L, 2L, 3, 0);
        final Line line = new Line(1L, lineName, lineColor, 0);
        final Section section = new Section(1L, station1, station2, 3, line);

        given(lineDao.save(lineRequest.toEntity())).willReturn(line);
        given(sectionDao.save(new Section(new Station(1L, ""), new Station(2L, ""), 3, line))).willReturn(section);
        given(stationDao.findById(section.getUpStation().getId())).willReturn(Optional.of(station1));
        given(stationDao.findById(section.getDownStation().getId())).willReturn(Optional.of(station2));
        given(sectionDao.findAllByLineId(1L)).willReturn(List.of(section));

        final LineResponse actual = lineService.createLine(lineRequest);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(LineResponse.from(line, List.of(station1, station2)));
    }

    @DisplayName("중복된 이름의 노선을 등록할 경우 예외를 발생한다.")
    @Test
    void createLine_throwsExceptionWithDuplicateName() {
        final LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 1L, 2L, 3, 0);
        given(lineDao.existByName("신분당선")).willReturn(true);

        assertThatThrownBy(() -> lineService.createLine(lineRequest))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 노선입니다.");
    }

    @DisplayName("등록된 모든 노선을 반환한다.")
    @Test
    void getAllLines() {
        final Line expectedLine1 = new Line(1L, "신분당선", "bg-red-600", 0);
        final Line expectedLine2 = new Line(2L, "분당선", "bg-yellow-600", 0);
        final List<LineResponse> expected = List.of(
                LineResponse.from(expectedLine1, Collections.emptyList()),
                LineResponse.from(expectedLine2, Collections.emptyList())
        );

        given(lineDao.findAll()).willReturn(List.of(expectedLine1, expectedLine2));

        assertThat(lineService.getAllLines()).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("노선 ID로 개별 노선을 찾아 반환한다.")
    @Test
    void getLineById() {
        final Line expected = new Line(1L, "신분당선", "bg-red-600", 0);
        given(lineDao.findById(1L)).willReturn(Optional.of(expected));

        final LineResponse actual = lineService.getLineById(1L);

        assertThat(actual).usingRecursiveComparison().
                isEqualTo(LineResponse.from(expected, Collections.emptyList()));
    }

    @DisplayName("노선 ID로 노선을 업데이트 한다.")
    @Test
    void updateLine() {
        final LineRequest lineRequest = new LineRequest("분당선", "bg-yellow-600", 1L, 2L, 5, 0);
        final Line newLine = new Line(1L, "분당선", "bg-yellow-600", 0);

        given(lineDao.findById(1L)).willReturn(Optional.of(newLine));

        lineService.update(1L, lineRequest);
        verify(lineDao, times(1)).update(1L, newLine);
    }

    @DisplayName("수정하려는 노선 ID가 존재하지 않을 경우 예외를 발생한다.")
    @Test
    void update_throwsExceptionIfLineIdIsNotExisting() {
        final LineRequest lineRequest = new LineRequest("분당선", "bg-yellow-600", 1L, 2L, 5, 0);
        given(lineDao.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                lineService.update(1L, lineRequest))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("존재하지 않는 노선 ID입니다.");
    }

    @DisplayName("등록된 노선을 삭제한다.")
    @Test
    void delete() {
        given(lineDao.deleteById(1L)).willReturn(1);
        lineService.delete(1L);
        verify(lineDao, times(1)).deleteById(1L);
    }

    @DisplayName("노선 등록 시, 지하철 역이 존재하지 않는다면 예외를 발생한다.")
    @Test
    void create_throwsExceptionIfStationsDoesNotExist() {
        final LineRequest lineRequest = new LineRequest("2호선", "bg-green-600", 1L, 2L, 10, 0);

        assertThatThrownBy(() -> lineService.createLine(lineRequest))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("존재하지 않는 지하철 역입니다.");
    }
}
