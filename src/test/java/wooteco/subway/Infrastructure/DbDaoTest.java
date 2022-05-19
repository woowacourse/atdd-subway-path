package wooteco.subway.Infrastructure;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/init.sql")
abstract public class DbDaoTest {
}
