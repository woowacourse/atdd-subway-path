package wooteco.subway.member.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wooteco.subway.member.dao.MemberDao;

class MemberServiceTest {

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMember() {
    }

    @Test
    void findMember() {
    }

    @Test
    void findMemberByEmail() {
    }

    @Test
    void updateMember() {
    }

    @Test
    void deleteMember() {
    }

    @Test
    void testFindMemberByEmail() {
    }
}