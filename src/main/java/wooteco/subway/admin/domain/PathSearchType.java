package wooteco.subway.admin.domain;

import java.util.Arrays;

public enum PathSearchType {
	DISTANCE("distance"),
	DURATION("duration");

	String name;

	PathSearchType(String name) {
		this.name = name;
	}

	public static PathSearchType of(String name) {
		return Arrays.stream(PathSearchType.values())
			.filter(type -> type.name.equals(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("해당 검색 타입이 존재하지 않습니다."));
	}
}
