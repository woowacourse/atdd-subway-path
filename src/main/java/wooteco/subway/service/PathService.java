package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.fare.AgeDisCountPolicy;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.domain.path.SubwayShortestPath;
import wooteco.subway.domain.path.PathInfo;
import wooteco.subway.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {

	private final LineRepository lineRepository;

	public PathService(LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	public PathInfo findPath(Station source, Station target, int age) {
		SubwayShortestPath shortestPath = new SubwayShortestPath(lineRepository.findAll());
		PathInfo path = shortestPath.find(source, target);
		return path.discountFare(AgeDisCountPolicy.from(age));
	}
}
