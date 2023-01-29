package com.example.springdatajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
/**
 * 
 * 등록시간,압데이트 시간 순수 jpa ㅎ
 * 
 * */
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @PrePersist // persist 하기전에 적용
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        updateDate = now;
    }

    @PreUpdate // 업데이트 전에 실행
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }


}
