package wooteco.subway.domain;

import java.util.List;

public interface Graph {

    /*
    1. 확인된 예외를 던진다 -> 확실하다 근데 조금...
    2. 미확인 예외를 던진다 -> JGraphtAdapter에서 던져야되고, 여기서 도메인 예외를 던져야된다.
    3. Empty List를 던진다 -> 이거는 확실.. 근데 예외를 던지는걸 생각해볼수도...
     */
    List<Station> findPath(Long source, Long target);

    int findDistance(Long source, Long target);
}
