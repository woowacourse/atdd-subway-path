package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.fare.AgeDisCountPolicy;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.domain.SubwayShortestPath;
import wooteco.subway.domain.PathInfo;
import wooteco.subway.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {

	private final LineRepository lineRepository;

	public PathService(LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	public PathInfo findPath(Station source, Station target, int age) {
		return new SubwayShortestPath(lineRepository.findAll())
			.find(source, target)
			.discountFare(AgeDisCountPolicy.from(age));
	}
}
