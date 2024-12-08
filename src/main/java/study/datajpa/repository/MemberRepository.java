package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

// @Repository -> 생략가능하다. 컴포넌트스캔 처리를 jpa가 자동으로 처리한다.
public interface MemberRepository extends JpaRepository<Member,Long> {
}
