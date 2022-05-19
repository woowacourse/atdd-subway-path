package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Lines {

	private final List<Line> value;

	public Lines(List<Line> value) {
		this.value = new ArrayList<>(value);
	}

	public Path findPath(Station source, Station target) {
		List<Section> sections = getSections();
		GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(initGraph(sections))
			.getPath(source, target);
		return new Path(path.getVertexList(), path.getWeight());
	}

	private List<Section> getSections() {
		return value.stream()
			.map(Line::getSections)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	private Graph<Station, DefaultWeightedEdge> initGraph(List<Section> sections) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph
			= new WeightedMultigraph<>(DefaultWeightedEdge.class);
		sections.forEach(section -> addSection(graph, section));
		return graph;
	}

	private void addSection(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
		graph.addVertex(section.getUpStation());
		graph.addVertex(section.getDownStation());
		graph.setEdgeWeight(
			graph.addEdge(section.getUpStation(), section.getDownStation()),
			section.getDistance()
		);
	}
}
