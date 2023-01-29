package com.example.springdatajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate // 등록일
    @Column(updatable = false) // 업데이트 하지못하게
    private LocalDateTime createDate;

    @LastModifiedDate // 수정일
    private LocalDateTime lastModifiedDate;
}
