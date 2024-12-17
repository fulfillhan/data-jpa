package study.datajpa.Controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    //빈이 생성되고 의존성 주입이 완료된 후 실행된다.
    @PostConstruct
    public void init(){
        //memberRepository.save(new Member("memberA"));
        for (int i = 0; i <100 ; i++) {
            memberRepository.save(new Member("member"+i+ " "+i));
        }
    }
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }


    /* 도메인 클래스 컨버터
    * http 요청을 회원 id로 받지만 도메인 클래스 컨버터가 중간에 작동해서 회원 엔티티 객체로 바꿔준다.
    * 주의 -> 조회용으로만 사용해야한다. member 객체를 변경하면 안된다.
    * */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
      //Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable){
        Page<Member> all = memberRepository.findAll(pageable);
        return all;
    }




}
