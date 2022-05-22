package wooteco.subway.ui.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.response.LineResponse;

@Service
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public LineResponse create(LineRequest lineRequest) {
        String name = lineRequest.getName();
        String color = lineRequest.getColor();
        Section section = getSection(lineRequest);
        Line line = new Line(name, color, section);
        return save(section, line);
    }

    private Section getSection(LineRequest lineRequest) {
        Station upStation = stationDao.findById(lineRequest.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        Station downStation = stationDao.findById(lineRequest.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        return new Section(upStation, downStation, lineRequest.getDistance());
    }

    private LineResponse save(Section section, Line line) {
        try {
            Line createdLine = lineDao.save(line);
            sectionDao.save(section, createdLine.getId());
            return new LineResponse(createdLine);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
        }
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(LineResponse::new)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        Line line = lineDao.findById(id).orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 노선이 존재하지 않습니다."));
        return new LineResponse(line);
    }

    public void modify(Long id, LineRequest lineRequest) {
        Line line = new Line(id, lineRequest.getName(), lineRequest.getColor());
        lineDao.update(line);
    }

    public void delete(Long id) {
        lineDao.delete(id);
    }
}
