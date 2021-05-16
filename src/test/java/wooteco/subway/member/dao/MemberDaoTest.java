package wooteco.subway.member.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import wooteco.subway.member.domain.Member;

@JdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final MemberDao memberDao;
    private Member originMember;

    MemberDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.memberDao = new MemberDao(jdbcTemplate, dataSource);
    }

    @BeforeEach
    void init() {
        Member member = new Member("test@test.com", "pass", 23);
        originMember = memberDao.insert(member);
    }

    @Test
    void update() {
        //given
        String newEmail = "newTest@test.com";
        String newPassWord = "new";
        int newAge = 20;

        //when
        memberDao.update(new Member(originMember.getId(), newEmail, newPassWord, newAge));
        Member findMember = memberDao.findByEmail(newEmail).get();

        //then
        assertThat(findMember.getEmail()).isEqualTo(newEmail);
        assertThat(findMember.getPassword()).isEqualTo(newPassWord);
        assertThat(findMember.getAge()).isEqualTo(newAge);
    }

    @Test
    void deleteById() {
        //given

        //when
        memberDao.deleteById(originMember.getId());

        //then
        assertThatThrownBy(() -> memberDao.findById(originMember.getId()))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }
}