package com.sikhye.chabak.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass    // 해당 클래스는 엔티티 속성 상속만을 목적으로 사용
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	// DB 자체적으로 갱신하도록 함
	// @CreatedDate
	private LocalDateTime createdAt;

	// @LastModifiedDate
	private LocalDateTime updatedAt;


}
