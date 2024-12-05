package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String userName;

    // 엔티티 기본적으로 기본생성자 있어야 하지만, protected 까지 열어둬야함. 프록싱기술사용할때 사용하기 위해서
    protected Member() {

    }

    public Member(String userName){
        this.userName = userName;
    }

}
