package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void testMember() {

        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        //memberRepository.getClass() = class jdk.proxy3.$Proxy127

        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();

        //검증
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    public void basicCrud() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //갯수 검증
        Long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제하기
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        //삭제후 갯수 검증
        Long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndGraterThan() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testQuery() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findUsers = memberRepository.findUser("AAA", 10);

        assertThat(findUsers.get(0)).isEqualTo(member1);

    }

    @Test
    public void findUsernameTest() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> userList = memberRepository.findUserList();
        for (String result : userList) {
            System.out.println("result = " + result);
        }

    }

    //dto로 조회하기
    @Test
    public void findUserDtoTest() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA", 10);
        memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }

    }

    @Test
    public void returnType() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        //컬렉션 조회 결과가 없으면 빈 컬렉션 반환
        List<Member> result = memberRepository.findListByUsername("AA");
        System.out.println("result = " + result);
        // 실행결과 0
    }

    // 페이징 조건과 정열 조건 설정
    @Test
    public void page() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //페이지를 유지하면서 엔티티->DTO로 변환하기
        Page<MemberDto> memberDtos = page.map(m-> new MemberDto(m.getId(),m.getUsername(),m.getTeam().getName()));

        List<Member> content = page.getContent(); // 0~3개의 내용을 가져온다.
        assertThat(content.size()).isEqualTo(3); //조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가
    }

    //벌크 수정 쿼리와 주의점 적용(영속성 컨텍스트를 고려해야 한다.)
    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 22));

        //주의점 -> 영속성 컨텍스트를 무시하기 때문에 영속성컨텍스트에 있는 데이터와 db의 데이터가 다르다(데이터의 정합성 문제)
        int resultCount = memberRepository.bulkAgePlus(20);
       /* em.flush();//남아 있는 데이터 db 반영한 후
        em.clear();//영속성 컨텍스트에 있는 데이터를 날린다.*/

        Member member5 = memberRepository.findMemberByUsername("member5");
        System.out.println("member5 = " + member5);  //실행결과 22

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //select member 쿼리만 진행됨
        //List<Member> members = memberRepository.findAll();
        //fetch join 성능 최적화(한번에 필요한 엔티티 쿼리를 가져온다-> 이때 가져오는 team 엔티티는 프록시가 아니라 진짜 객체로 즉시로딩함)
        List<Member> members = memberRepository.findMemberFetchJoin();
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            //연관된 데이터를 가져오려고 해서 team 쿼리를 추가해서 n+1 문제 발생
            //team 프록시 객체로 가져옴
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }

    }


}