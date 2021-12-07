package com.sikhye.chabak.service.tag.entity;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.sikhye.chabak.global.constant.BaseStatus;
import com.sikhye.chabak.service.post.entity.Posting;

import lombok.Builder;
import lombok.Getter;

@Getter
@DynamicInsert
@Entity
@Table(name = "PostingTag")
public class PostingTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "posting_id")
	private Long postingId;

	@Enumerated(EnumType.STRING)
	private BaseStatus status;

	public PostingTag() {

	}

	public void setStatusToDelete() {
		this.status = BaseStatus.DELETED;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Builder
	public PostingTag(String name, Long postingId) {
		this.name = name;
		this.postingId = postingId;
	}

	// 연관관계 매핑
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "POSTING_ID")
	private Posting posting;

}
