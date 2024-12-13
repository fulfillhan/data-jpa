package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

//순수 jpa 활용
@MappedSuperclass
@Getter
public class JpaBasisEntity {
    @Column(updatable = false)// d에 값이 실수로 수정되지 않도록 한다.
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    //이벤트 메서드
    @PrePersist //영속성 persist 하기 전에 이벤트 발생
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate
    public void preUpdate(){
        //업데이트 날짜만 갱신
        updatedDate = LocalDateTime.now();
    }
}
