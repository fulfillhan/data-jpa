package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
//스프링 데이터 Auditing 적용
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity extends BaseTimeEntity {

    //등록자, 수정자
    @CreatedBy
    @Column(updatable = false)
    private String createBy;
    @LastModifiedBy
    private String lastModifiedBy;
}
