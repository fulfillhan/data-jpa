package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

//스프링 데이터 jpa 적용
// @Repository -> 생략가능하다. 컴포넌트스캔 처리를 jpa가 자동으로 처리한다.
public interface MemberRepository extends JpaRepository<Member,Long> {

    //1. 메서드 이름으로 쿼리 생성
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    //2.Query 리포지토리 메서드 위에 쿼리를 바로 정의하기
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUserList();


    //dto로 반환하기
    @Query("select new study.datajpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team")
    List<MemberDto> findMemberDto();

    //컬렉션 타입으로 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names")List<String> names);

    //반환타입도 여러개 지원
    List<Member> findListByUsername(String name);
    Member findMemberByUsername(String name);
    Optional<Member> findOptionalByUsername(String name);

}
